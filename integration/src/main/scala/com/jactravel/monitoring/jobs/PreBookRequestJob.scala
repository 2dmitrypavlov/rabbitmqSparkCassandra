package com.jactravel.monitoring.jobs

import com.jactravel.monitoring.model.jobs.PreBookRequestJobInfo._
import com.pygmalios.reactiveinflux._
import com.pygmalios.reactiveinflux.spark._
import org.joda.time.DateTime

import scala.concurrent.duration._
import scala.util.Try

/**
  * Created by fayaz on 09.07.17.
  */
object PreBookRequestJob extends JobConfig("pre-book-request-job") {

  def main(args: Array[String]): Unit = {

    val nullFilter = Seq("time", "brand_name", "sales_channel", "trade_parent_group", "trade_name", "trade_group", "xml_booking_login")

    import spark.implicits._

    // BRAND TABLE
    spark
      .read
      .format("com.databricks.spark.csv")
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load(aws + "brand.csv")
      .createOrReplaceTempView("Brand")

    // TRADE TABLE
    spark
      .read
      .format("com.databricks.spark.csv")
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load(aws + "trade.csv")
      .createOrReplaceTempView("Trade")

    // SALES CHANNEL TABLE
    spark
      .read
      .format("com.databricks.spark.csv")
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load(aws + "saleschannel.csv")
      .createOrReplaceTempView("SalesChannel")

    // QUERY PROXY REQUEST
    spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map(
        "table" -> "query_proxy_request_second",
        "keyspace" -> "jactravel_monitoring_new"))
      .load()
      .filter(query)
      .createOrReplaceTempView("QueryProxyRequest")

    // PRE BOOK REQUEST
    spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map(
        "table" -> "pre_book_request_second",
        "keyspace" -> "jactravel_monitoring_new"))
      .load()
      .filter(query)
      .createOrReplaceTempView("PurePreBookRequest")

    // RICH PRE BOOK REQUEST
    spark.sql(
      """SELECT pbr.query_uuid,
                brand_name,
                trade_name,
                trade_group,
                trade_parent_group,
                sales_channel,
                (unix_timestamp(end_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.sss') - unix_timestamp(start_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.S')) AS response_time_ms,
                pbr.error_stack_trace,
                pbr.success,
                xml_booking_login,
                window(start_utc_timestamp, '5 minutes').end as time
         FROM PurePreBookRequest AS pbr,
              SalesChannel AS sc,
              Trade AS t,
              Brand AS b
         LEFT JOIN QueryProxyRequest AS qpr
         ON pbr.query_uuid == qpr.query_uuid
         WHERE pbr.sales_channel_id == sc.sales_channel_id
         AND pbr.trade_id == t.trade_id
         AND pbr.brand_id == b.brand_id"""
    ).createOrReplaceTempView("RichPreBookRequest")

    // PRE BOOK COUNT
    val preBookCount = spark.sql(
      """SELECT COUNT(query_uuid) as pre_book_count,
            time,
            brand_name,
            sales_channel,
            trade_group,
            trade_name,
            trade_parent_group,
            xmL_booking_login
        FROM RichPreBookRequest
        GROUP BY
            time,
            brand_name,
            sales_channel,
            trade_group,
            trade_name,
            trade_parent_group,
            xml_booking_login""")
      .na.fill("stub", nullFilter)
      .as[PreBookRequestCount]

    // PRE BOOK SUCCESS
    val preBookSuccess = spark.sql(
      """SELECT COUNT(query_uuid) as success_count,
            time,
            brand_name,
            sales_channel,
            trade_group,
            trade_name,
            trade_parent_group,
            xmL_booking_login
        FROM RichPreBookRequest
        WHERE success IS NOT NULL
        GROUP BY
            time,
            brand_name,
            sales_channel,
            trade_group,
            trade_name,
            trade_parent_group,
            xml_booking_login""")
      .na.fill("stub", nullFilter)
      .as[PreBookRequestSuccessCount]

    // PRE BOOK ERROR
    val preBookError = spark.sql(
      """SELECT COUNT(query_uuid) as errors_count,
            time,
            brand_name,
            sales_channel,
            trade_group,
            trade_name,
            trade_parent_group,
            xml_booking_login
        FROM RichPreBookRequest
        WHERE error_stack_trace IS NOT NULL
        GROUP BY
            time,
            brand_name,
            sales_channel,
            trade_group,
            trade_name,
            trade_parent_group,
            xml_booking_login""")
      .na.fill("stub", nullFilter)
      .as[PreBookRequestErrorsCount]

    // BOOK RESPONSE TIME
    val preBookResponseTime = spark.sql(
      """
        SELECT time,
            brand_name,
            sales_channel,
            trade_group,
            trade_name,
            trade_parent_group,
            xmL_booking_login,
            min(response_time_ms) as min_response_time_ms,
            max(response_time_ms) as max_response_time_ms,
            percentile_approx(response_time_ms, 0.5) as perc_response_time_ms
        FROM RichPreBookRequest
        GROUP BY
            time,
            brand_name,
            sales_channel,
            trade_group,
            trade_name,
            trade_parent_group,
            xml_booking_login""")
      .na.fill("stub", nullFilter)
      .as[PreBookRequestResponseTime]


    // SAVING TO INFLUXDB

    preBookCount.rdd.map { src =>
      com.pygmalios.reactiveinflux.Point(
        time = DateTime.now(),
        measurement = "pre_book_request_count",
        tags = Map(
          "brand_name" -> Try(src.brand_name).getOrElse("no_brand")
          , "trade_group" -> Try(src.trade_group).getOrElse("no_group")
          , "trade_name" -> Try(src.trade_name).getOrElse("no_trade_name")
          , "trade_parent_group" -> Try(src.trade_parent_group).getOrElse("no_trade")
          , "xml_booking_login" -> Try(src.xml_booking_login).getOrElse("no_xml")
          , "sales_channel" -> Try(src.sales_channel).getOrElse("no_xml")
        ),
        fields = Map(
          "pre_book_count" -> Try(src.pre_book_count.toInt).getOrElse(1)
        )
      )
    }.saveToInflux()

    preBookSuccess.rdd.map { src =>
      com.pygmalios.reactiveinflux.Point(
        time = DateTime.now(),
        measurement = "pre_book_success_count",
        tags = Map(
          "brand_name" -> Try(src.brand_name).getOrElse("no_brand")
          , "trade_group" -> Try(src.trade_group).getOrElse("no_group")
          , "trade_name" -> Try(src.trade_name).getOrElse("no_trade_name")
          , "trade_parent_group" -> Try(src.trade_parent_group).getOrElse("no_trade")
          , "xml_booking_login" -> Try(src.xml_booking_login).getOrElse("no_xml")
          , "sales_channel" -> Try(src.sales_channel).getOrElse("no_sales")
        ),
        fields = Map(
          "success_count" -> Try(src.success_count.toInt).getOrElse(1)
        )
      )
    }.saveToInflux()

    preBookError.rdd.map { src =>
      com.pygmalios.reactiveinflux.Point(
        time = DateTime.now(),
        measurement = "pre_book_errors_count",
        tags = Map(
          "brand_name" -> Try(src.brand_name).getOrElse("no_brand")
          , "trade_group" -> Try(src.trade_group).getOrElse("no_group")
          , "trade_name" -> Try(src.trade_name).getOrElse("no_trade_name")
          , "trade_parent_group" -> Try(src.trade_parent_group).getOrElse("no_trade")
          , "xml_booking_login" -> Try(src.xml_booking_login).getOrElse("no_xml")
          , "sales_channel" -> Try(src.sales_channel).getOrElse("no_sales")
        ),
        fields = Map(
          "errors_count" -> Try(src.errors_count.toInt).getOrElse(1)
        )
      )
    }.saveToInflux()

    preBookResponseTime.rdd.map { src =>
      com.pygmalios.reactiveinflux.Point(
        time = DateTime.now(),
        measurement = "pre_book_response_time",
        tags = Map(
          "brand_name" -> Try(src.brand_name).getOrElse("no_brand")
          , "trade_group" -> Try(src.trade_group).getOrElse("no_group")
          , "trade_name" -> Try(src.trade_name).getOrElse("no_trade_name")
          , "trade_parent_group" -> Try(src.trade_parent_group).getOrElse("no_trade")
          , "xml_booking_login" -> Try(src.xml_booking_login).getOrElse("no_xml")
          , "sales_channel" -> Try(src.sales_channel).getOrElse("no_sales_channel")
        ),
        fields = Map(
          "min_response_time" -> Try(src.min_response_time_ms.toInt).getOrElse(1)
          , "max_response_time" -> Try(src.max_response_time_ms.toInt).getOrElse(1)
          , "perc_response_time" -> Try(src.perc_response_time_ms.toInt).getOrElse(1)
        )
      )
    }.saveToInflux()


  }

}
