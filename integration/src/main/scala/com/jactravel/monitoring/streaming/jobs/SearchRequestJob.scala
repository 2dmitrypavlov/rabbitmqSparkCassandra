package com.jactravel.monitoring.streaming.jobs

import com.jactravel.monitoring.model.jobs.SearchRequestJobInfo
import com.jactravel.monitoring.streaming.ConfigService
import com.paulgoldbaum.influxdbclient.InfluxDB

import scala.concurrent.Await

/**
  * Created by fayaz on 09.07.17.
  */
object SearchRequestJob  extends ConfigService with SearchRequestJobInfo with BaseJob {

  override val appName: String = "search_request_job"

  def main(args: Array[String]): Unit = {

    val nullFilter = Seq("time","brand_name", "sales_channel", "trade_parent_group", "trade_name", "trade_group", "xml_booking_login")

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
    val searchSuccess = spark.sql("""
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
    val searchErrors = spark.sql("""
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
    val searchResponseTime = spark.sql("""
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
      .na.fill("stub", Seq("time","brand_name", "sales_channel", "trade_parent_group", "trade_name", "trade_group", "xml_booking_login"))
      .as[SearchRequestResponseTime]

    // SAVING TO INFLUXDB

    // SAVING BOOK COUNT TO INFLUXDB
    searchCount.foreachPartition { partition =>

      // Open connection to Influxdb
      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)

      partition
        .map(toSearchCountPoint)
        .foreach(p => Await.result(db.write(p), timeout))

      // Close connection
      db.close()
    }

    // SAVING BOOK SUCCESS TO INFLUXDB
    searchSuccess.foreachPartition { partition =>

      // Open connection to Influxdb
      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)

      partition
        .map(toSuccessCountPoint)
        .foreach(p => Await.result(db.write(p), timeout))

      // Close connection
      db.close()
    }

    // SAVING BOOK ERROR TO INFLUXDB
    searchErrors.foreachPartition { partition =>

      // Open connection to Influxdb
      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)

      partition
        .map(toErrorsCountPoint)
        .foreach(p => Await.result(db.write(p), timeout))

      // Close connection
      db.close()
    }

    // SAVING BOOK RESPONSE TO INFLUXDB
    searchResponseTime.foreachPartition { partition =>

      // Open connection to Influxdb
      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)

      partition
        .map(toResponseTimePoint)
        .foreach(p => Await.result(db.write(p), timeout))

      // Close connection
      db.close()
    }

    spark.stop()
  }
}
