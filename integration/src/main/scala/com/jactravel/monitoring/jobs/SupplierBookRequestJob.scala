package com.jactravel.monitoring.jobs

import com.jactravel.monitoring.model.jobs.SupplierBookRequestJobInfo._
import com.pygmalios.reactiveinflux._
import com.pygmalios.reactiveinflux.spark._
import org.joda.time.DateTime

import scala.util.Try

/**
  * Created by fayaz on 09.07.17.
  */
object SupplierBookRequestJob extends JobConfig("supplier-book-request-job") {

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

    // BOOK REQUEST
    spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map(
        "table" -> "book_request_second",
        "keyspace" -> "jactravel_monitoring_new"))
      .load()
      .filter(query)
      .createOrReplaceTempView("BookRequest")

    // SUPPLIER PRE BOOK REQUEST
    spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map("table" -> "supplier_book_request_second", "keyspace" -> "jactravel_monitoring_new"))
      .load()
      .filter(query)
      .createOrReplaceTempView("SupplierBookRequest")

    // QUERY PROXY REQUEST
    spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map("table" -> "query_proxy_request_second", "keyspace" -> "jactravel_monitoring_new"))
      .load()
      .filter(query)
      .createOrReplaceTempView("QueryProxyRequest")

    // RICH SUPPLIER PRE BOOK REQUEST
    spark.sql(
      """
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
      FROM SupplierBookRequest as spr,
           BookRequest as pbr,
           Brand as br,
           SalesChannel as sc,
           Trade as tr
       left join QueryProxyRequest qpr on spr.query_uuid = qpr.query_uuid
       WHERE spr.query_uuid = pbr.query_uuid
       AND pbr.brand_id = br.brand_id
       AND pbr.sales_channel_id = sc.sales_channel_id
       AND pbr.trade_id = tr.trade_id""")
      .createOrReplaceTempView("RichSupplierBookRequest")

    val supplierBookRequestGraph = spark.sql(
      """
      SELECT
          sum(success) as success_rate,
          count(query_uuid) as book_count,
          percentile_approx(process_time_ms, 0.5) as processing_time_ms_50,
          percentile_approx(process_time_ms, 0.95) as processing_time_ms_95,
          percentile_approx(process_time_ms, 0.99) as processing_time_ms_99,
          source,
          xml_booking_login,
          trade_name,
          brand_name,
          sales_channel
      FROM RichSupplierBookRequest
      GROUP BY
          source,
          xml_booking_login,
          trade_name,
          brand_name,
          sales_channel""")
      .na.fill("stub", Seq("time", "brand_name", "sales_channel", "trade_parent_group", "trade_name", "trade_group", "xml_booking_login"))
      .as[SupplierBookRequestInfo]

    // SAVING TO INFLUXDB

    supplierBookRequestGraph.rdd.map { src =>
      com.pygmalios.reactiveinflux.Point(
        time = DateTime.now(),
        measurement = "supplier_book_request",
        tags = Map(
          "brand_name" -> Try(src.brand_name).getOrElse("no_brand")
          , "source" -> Try(src.source).getOrElse("no_source")
          , "trade_name" -> Try(src.trade_name).getOrElse("no_trade_name")
          , "xml_booking_login" -> Try(src.xml_booking_login).getOrElse("no_xml")
          , "sales_channel" -> Try(src.sales_channel).getOrElse("no_sales")
        ),
        fields = Map(
          "book_count" -> Try(src.book_count.toInt).getOrElse(1)
          , "processing_time_ms_50" -> Try(src.processing_time_ms_50).getOrElse(1.0)
          , "processing_time_ms_95" -> Try(src.processing_time_ms_95).getOrElse(1.0)
          , "processing_time_ms_99" -> Try(src.processing_time_ms_99).getOrElse(1.0)
        )
      )
    }.saveToInflux()


  }
}
