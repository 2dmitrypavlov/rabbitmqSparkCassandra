package com.jactravel.monitoring.streaming.jobs

import com.jactravel.monitoring.model.jobs.SupplierPreBookRequestJobInfo
import com.jactravel.monitoring.streaming.ConfigService
import com.paulgoldbaum.influxdbclient.InfluxDB

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by fayaz on 09.07.17.
  */
object SupplierPreBookRequestJob extends ConfigService with SupplierPreBookRequestJobInfo with BaseJob {

  override val appName: String = "supplier_pre_book_request_job"

  def main(args: Array[String]): Unit = {

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

    // PRE BOOK REQUEST
    spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map(
        "table" -> "pre_book_request_second",
        "keyspace" -> "jactravel_monitoring_new"))
      .load()
      .filter(query)
      .createOrReplaceTempView("PreBookRequest")

    // SUPPLIER PRE BOOK REQUEST
    spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map( "table" -> "supplier_pre_book_request_second", "keyspace" -> "jactravel_monitoring_new"))
      .load()
      .filter(query)
      .createOrReplaceTempView("SupplierPreBookRequest")

    // QUERY PROXY REQUEST
    spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map( "table" -> "query_proxy_request_second", "keyspace" -> "jactravel_monitoring_new"))
      .load()
      .filter(query)
      .createOrReplaceTempView("QueryProxyRequest")

    // RICH SUPPLIER PRE BOOK REQUEST
    spark.sql("""
      SELECT
          qpr.query_uuid,
          spr.source,
          qpr.xml_booking_login,
          tr.trade_name,
          br.brand_name,
          sc.sales_channel,
          unix_timestamp(spr.end_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.sss') - unix_timestamp(spr.start_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.sss') as process_time_ms,
          spr.success,
          window(pbr.start_utc_timestamp, '5 minute').end as time
      FROM SupplierPreBookRequest as spr,
           PreBookRequest as pbr,
           Brand as br,
           SalesChannel as sc,
           Trade as tr
       left join QueryProxyRequest qpr on spr.query_uuid = qpr.query_uuid
       WHERE spr.query_uuid = pbr.query_uuid
       AND pbr.brand_id = br.brand_id
       AND pbr.sales_channel_id = sc.sales_channel_id
       AND pbr.trade_id = tr.trade_id""")
      .createOrReplaceTempView("RichSupplierPreBookRequest")

    // SUPPLIER PRE BOOK GRAPH
    val supplierPreBookGraph = spark.sql("""
      SELECT
          sum(success) as success_rate,
          count(query_uuid) as pre_book_count,
          percentile_approx(process_time_ms, 0.5) as processing_time_ms_50,
          percentile_approx(process_time_ms, 0.95) as processing_time_ms_95,
          percentile_approx(process_time_ms, 0.99) as processing_time_ms_99,
          source,
          xml_booking_login,
          trade_name,
          brand_name,
          sales_channel
      FROM RichSupplierPreBookRequest
      GROUP BY
          source,
          xml_booking_login,
          trade_name,
          brand_name,
          sales_channel""")
      .na.fill("stub", Seq("time","brand_name", "sales_channel", "trade_parent_group", "trade_name", "trade_group", "xml_booking_login"))
      .as[SupplierPreBookRequestInfo]

    // SAVING TO INFLUXDB

    // SAVING SUPPLIER GRAPH TO INFLUXDB
    supplierPreBookGraph.foreachPartition { partition =>

      // Open connection to Influxdb
      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)

      partition
        .map(toPoint)
        .foreach(p => Await.result(db.write(p), influxTimeout))

      // Close connection
      db.close()
    }

    spark.stop()
  }
}
