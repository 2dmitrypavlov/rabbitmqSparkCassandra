package com.jactravel.monitoring.streaming.jobs

import com.jactravel.monitoring.model.jobs.BookRequestJobInfo
import com.jactravel.monitoring.model.jobs.CaseClassed._
import com.jactravel.monitoring.streaming.ConfigService
import com.paulgoldbaum.influxdbclient._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by fayaz on 09.07.17.
  */
object BookRequestJob extends ConfigService with BookRequestJobInfo {


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

    // BOOK REQUEST
    spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map(
        "table" -> "book_request_second",
        "keyspace" -> "jactravel_monitoring_new"))
      .load()
      .filter(query)
      .createOrReplaceTempView("PureBookRequest")

    // RICH BOOK REQUEST
    spark.sql(
      """SELECT
                br.query_uuid AS query_uuid,
                brand_name,
                trade_name,
                trade_group,
                trade_parent_group,
                sales_channel,
                (unix_timestamp(end_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.sss') - unix_timestamp(start_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.S')) AS response_time_ms,
                br.error_stack_trace,
                br.success,
                xml_booking_login,
                window(start_utc_timestamp, '5 minutes').end AS time
              FROM PureBookRequest AS br,
                SalesChannel AS sc,
                Trade AS t,
                Brand AS b
              LEFT JOIN QueryProxyRequest AS qpr
              ON br.query_uuid == qpr.query_uuid
              WHERE br.sales_channel_id == sc.sales_channel_id
              AND br.trade_id == t.trade_id
              AND br.brand_id == b.brand_id
          """
    ).createOrReplaceTempView("RichBookRequest")


    // BOOK COUNT
    val bookCount = spark.sql(
      """SELECT COUNT(query_uuid) as book_count,
                  time,
                  brand_name,
                  sales_channel,
                  trade_group,
                  trade_name,
                  trade_parent_group,
                  xmL_booking_login
              FROM RichBookRequest
              GROUP BY
                  time,
                  brand_name,
                  sales_channel,
                  trade_group,
                  trade_name,
                  trade_parent_group,
                  xml_booking_login"""
    ).na.fill("stub", nullFilter :+ "book_count")
      .as[BookRequestCount]

    // BOOK SUCCESS
    val bookSuccess = spark.sql(
      """SELECT COUNT(query_uuid) as success_count,
                  time,
                  brand_name,
                  sales_channel,
                  trade_group,
                  trade_name,
                  trade_parent_group,
                  xmL_booking_login
              FROM RichBookRequest
              WHERE success IS NOT NULL
              GROUP BY
                  time,
                  brand_name,
                  sales_channel,
                  trade_group,
                  trade_name,
                  trade_parent_group,
                  xml_booking_logiN"""
    ).na.fill("stub", nullFilter :+ "success_count")
      .as[BookRequestSuccessCount]

    // BOOK ERROR
    val bookError = spark.sql(
      """SELECT COUNT(query_uuid) as errors_count,
                  time,
                  brand_name,
                  sales_channel,
                  trade_group,
                  trade_name,
                  trade_parent_group,
                  xml_booking_login
              FROM RichBookRequest
              WHERE error_stack_trace IS NOT NULL
              GROUP BY
                  time,
                  brand_name,
                  sales_channel,
                  trade_group,
                  trade_name,
                  trade_parent_group,
                  xml_booking_login"""
    ).na.fill("stub", nullFilter :+ "errors_count")
      .as[BookRequestErrorsCount]

    // BOOK RESPONSE TIME
    val bookResponseTime = spark.sql(
      """SELECT time,
                  brand_name,
                  sales_channel,
                  trade_group,
                  trade_name,
                  trade_parent_group,
                  xmL_booking_login,
                  min(response_time_ms) as min_response_time_ms,
                  max(response_time_ms) as max_response_time_ms,
                  percentile_approx(response_time_ms, 0.5) as perc_response_time_ms
              FROM RichBookRequest
              GROUP BY
                  time,
                  brand_name,
                  sales_channel,
                  trade_group,
                  trade_name,
                  trade_parent_group,
                  xml_booking_login"""
    ).na.fill("stub", nullFilter :+ "min_response_time_ms" :+ "max_response_time_ms" :+ "perc_response_time_ms")
      .as[BookRequestResponseTime]

    // SAVING TO INFLUXDB

    // SAVING BOOK COUNT TO INFLUXDB
    bookCount.foreachPartition { partition =>

      // Open connection to Influxdb
      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)

      partition
        .map(toBookCountPoint)
        .foreach(p => Await.result(db.write(p), influxTimeout))

      // Close connection
      db.close()
    }

    // SAVING BOOK SUCCESS TO INFLUXDB
    bookSuccess.foreachPartition { partition =>

      // Open connection to Influxdb
      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)

      partition
        .map(toSuccessCountPoint)
        .foreach(p => Await.result(db.write(p), influxTimeout))

      // Close connection
      db.close()
    }

    // SAVING BOOK ERROR TO INFLUXDB
    bookError.foreachPartition { partition =>

      // Open connection to Influxdb
      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)

      partition
        .map(toErrorsCountPoint)
        .foreach(p => Await.result(db.write(p), influxTimeout))

      // Close connection
      db.close()
    }

    // SAVING BOOK RESPONSE TO INFLUXDB
    bookResponseTime.foreachPartition { partition =>

      // Open connection to Influxdb
      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)

      partition
        .map(toResponseTimePoint)
        .foreach(p => Await.result(db.write(p), influxTimeout))

      // Close connection
      db.close()
    }

    spark.stop()
  }
}
