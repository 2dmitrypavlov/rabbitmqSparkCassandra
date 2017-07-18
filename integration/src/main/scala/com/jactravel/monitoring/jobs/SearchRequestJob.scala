package com.jactravel.monitoring.jobs

import com.jactravel.monitoring.model.jobs.SearchRequestJobInfo._

import scala.util.Try
import com.pygmalios.reactiveinflux._
import com.pygmalios.reactiveinflux.spark._
import org.joda.time.DateTime

import scala.concurrent.duration._

/**
  * Created by fayaz on 09.07.17.
  */
object SearchRequestJob extends JobConfig("search-request-job") {

  def main(args: Array[String]): Unit = {

    val nullFilter = Seq("time", "brand_name", "sales_channel", "trade_parent_group", "trade_name", "trade_group", "xml_booking_login")

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

    // SEARCH REQUEST
    spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map(
        "table" -> "search_request_second",
        "keyspace" -> "jactravel_monitoring_new"))
      .load()
      .filter(query)
      .createOrReplaceTempView("PureSearchRequest")
    import spark.implicits._
    // SEARCH REQUEST
    spark.sql(
      """SELECT sr.query_uuid,
               brand_name,
               trade_name,
               trade_group,
               trade_parent_group,
               sales_channel,
              (unix_timestamp(request_info.end_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.sss') - unix_timestamp(request_info.start_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.sss')) AS response_time_ms,
               sr.response_info.error_stack_trace,
               sr.response_info.success,
               xml_booking_login,
               window(request_info.start_utc_timestamp, '5 minutes').end as time
        FROM PureSearchRequest as sr,
             SalesChannel as sc,
             Trade as t,
             Brand as b
        LEFT JOIN QueryProxyRequest as qpr
        ON sr.query_uuid == qpr.query_uuid
        WHERE sr.request_info.sales_channel_id == sc.sales_channel_id
        AND sr.request_info.trade_id == t.trade_id
        AND sr.request_info.brand_id == b.brand_id""")
      .createOrReplaceTempView("RichSearchRequest")

    // SEARCH COUNT
    val searchCount = spark.sql(
      """SELECT COUNT(query_uuid) as search_count,
            time,
            brand_name,
            sales_channel,
            trade_group,
            trade_name,
            trade_parent_group,
            xmL_booking_login
        FROM RichSearchRequest
        GROUP BY
            time,
            brand_name,
            sales_channel,
            trade_group,
            trade_name,
            trade_parent_group,
            xml_booking_login""")
      .na.fill("stub", nullFilter)
      .as[SearchRequestCount]

    // SEARCH SUCCESS
    val searchSuccess = spark.sql(
      """
        SELECT COUNT(query_uuid) as success_count,
            time,
            brand_name,
            sales_channel,
            trade_group,
            trade_name,
            trade_parent_group,
            xmL_booking_login
        FROM RichSearchRequest
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
      .as[SearchRequestSuccess]

    // SEARCH ERROR
    val searchErrors = spark.sql(
      """
        SELECT COUNT(query_uuid) as errors_count,
            time,
            brand_name,
            sales_channel,
            trade_group,
            trade_name,
            trade_parent_group,
            xml_booking_login
        FROM RichSearchRequest
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
      .as[SearchRequestErrors]

    // SEARCH RESPONSE TIME
    val searchResponseTime = spark.sql(
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
        FROM RichSearchRequest
        GROUP BY
            time,
            brand_name,
            sales_channel,
            trade_group,
            trade_name,
            trade_parent_group,
            xml_booking_login""")
      .na.fill("stub", Seq("time", "brand_name", "sales_channel", "trade_parent_group", "trade_name", "trade_group", "xml_booking_login"))
      .as[SearchRequestResponseTime]

    // SAVING TO INFLUXDB

    searchCount.rdd.map { src =>
      com.pygmalios.reactiveinflux.Point(
        time = DateTime.now(),
        measurement = "search_request_count",
        tags = Map(
          "brand_name" -> Try(src.brand_name).getOrElse("no_brand")
          , "trade_group" -> Try(src.trade_group).getOrElse("no_group")
          , "trade_name" -> Try(src.trade_name).getOrElse("no_trade_name")
          , "trade_parent_group" -> Try(src.trade_parent_group).getOrElse("no_trade")
          , "xml_booking_login" -> Try(src.xml_booking_login).getOrElse("no_xml")
          , "sales_channel" -> Try(src.sales_channel).getOrElse("no_xml")
        ),
        fields = Map(
          "search_count" -> Try(src.search_count.toInt).getOrElse(1)
        )
      )
    }.saveToInflux()


    searchSuccess.rdd.map { src =>
      com.pygmalios.reactiveinflux.Point(
        time = DateTime.now(),
        measurement = "search_success_count",
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

    searchErrors.rdd.map { src =>
      com.pygmalios.reactiveinflux.Point(
        time = DateTime.now(),
        measurement = "search_errors_count",
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

    searchResponseTime.rdd.map { src =>
      com.pygmalios.reactiveinflux.Point(
        time = DateTime.now(),
        measurement = "search_response_time",
        tags = Map(
          "brand_name" -> Try(src.brand_name).getOrElse("no_brand")
          , "trade_group" -> Try(src.trade_group).getOrElse("no_group")
          , "trade_name" -> Try(src.trade_name).getOrElse("no_trade_name")
          , "trade_parent_group" -> Try(src.trade_parent_group).getOrElse("no_trade")
          , "xml_booking_login" -> Try(src.xml_booking_login).getOrElse("no_xml")
          , "sales_channel" -> Try(src.sales_channel).getOrElse("no_sales_channel")
        ),
        fields = Map(
          "min_response_time" -> Try(Math.abs(src.min_response_time_ms.toInt)).getOrElse(1)
          , "max_response_time" -> Try(src.max_response_time_ms.toInt).getOrElse(1)
          , "perc_response_time" -> Try(src.perc_response_time_ms.toInt).getOrElse(1)
        )
      )
    }.saveToInflux()


    //    "brand_name" -> Try(src.getAs("brand_name")).getOrElse("no_brand"),
    //    "trade_group" -> Try(src.getAs("trade_group")).getOrElse("no_group"),
    //    "trade_name" -> Try(src.getAs("trade_name")).getOrElse("no_trade_name"),
    //    "trade_parent_group" -> Try(src.getAs[String]("trade_parent_group")).getOrElse("no_trade"),
    //    "xml_booking_login" -> Try(src.getAs("xml_booking_login")).getOrElse("no_xml")
    //    ),
    //    fields      = Map(
    //      "search_count" -> Try(src.getAs[Int]("search_count")).getOrElse(11111111))

    //    // SAVING BOOK COUNT TO INFLUXDB
    //    searchCount.foreachPartition { partition =>
    //
    //      // Open connection to Influxdb
    //      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)
    //
    //      partition
    //        .map(toSearchCountPoint)
    //        .foreach(p => Try(Await.result(db.write(p), influxTimeout)))
    //
    //      // Close connection
    //      db.close()
    //    }


    //    // SAVING BOOK SUCCESS TO INFLUXDB
    //    searchSuccess.foreachPartition { partition =>
    //
    //      // Open connection to Influxdb
    //      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)
    //
    //      partition
    //        .map(toSuccessCountPoint)
    //        .foreach(p => Try(Await.result(db.write(p), influxTimeout)))
    //
    //      // Close connection
    //      db.close()
    //    }
    //
    //    // SAVING BOOK ERROR TO INFLUXDB
    //    searchErrors.foreachPartition { partition =>
    //
    //      // Open connection to Influxdb
    //      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)
    //
    //      partition
    //        .map(toErrorsCountPoint)
    //        .foreach(p => Try(Await.result(db.write(p), influxTimeout)))
    //
    //      // Close connection
    //      db.close()
    //    }
    //
    //    // SAVING BOOK RESPONSE TO INFLUXDB
    //    searchResponseTime.foreachPartition { partition =>
    //
    //      // Open connection to Influxdb
    //      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)
    //
    //      partition
    //        .map(toResponseTimePoint)
    //        .foreach(p => Try(Await.result(db.write(p), influxTimeout)))
    //
    //      // Close connection
    //      db.close()
    //    }

    //    spark.stop()
  }
}
