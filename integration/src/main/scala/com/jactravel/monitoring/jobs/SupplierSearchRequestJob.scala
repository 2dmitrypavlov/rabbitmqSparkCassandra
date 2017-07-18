package com.jactravel.monitoring.jobs

import com.jactravel.monitoring.model.jobs.SupplierSearchRequestJobInfo._
import com.pygmalios.reactiveinflux._
import com.pygmalios.reactiveinflux.spark._
import org.joda.time.DateTime

import scala.util.Try

/**
  * Created by fayaz on 09.07.17.
  */
object SupplierSearchRequestJob extends JobConfig("supplier-search-request-job") {

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
        "table" -> "search_request_second",
        "keyspace" -> "jactravel_monitoring_new"))
      .load()
      .filter(query)
      .createOrReplaceTempView("PureSearchRequest")

    // SUPPLIER BOOK REQUEST
    spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map("table" -> "supplier_search_request_second", "keyspace" -> "jactravel_monitoring_new"))
      .load()
      .filter(query)
      .createOrReplaceTempView("PureSupplierSearchRequest")

    // QUERY PROXY REQUEST
    spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map("table" -> "query_proxy_request_second", "keyspace" -> "jactravel_monitoring_new"))
      .load()
      .filter(query)
      .createOrReplaceTempView("QueryProxyRequest")

    // RICH SUPPLIER BOOK REQUEST
    spark.sql("""
        SELECT  psr.query_uuid,
                psr.host,
                source,
                psr.request_info.start_utc_timestamp,
                psr.request_info.end_utc_timestamp,
                timeout,
                property_count,
                psr.response_info.success,
                psr.response_info.error_message,
                psr.response_info.error_stack_trace,
                request_count,
                xml_booking_login,
                tr.trade_name,
                tr.trade_group,
                tr.trade_parent_group,
                br.brand_name,
                sc.sales_channel,
                window(request_info.start_utc_timestamp, '5 minutes').end AS time
        FROM PureSearchRequest AS psr,
             QueryProxyRequest as qpr,
             SalesChannel AS sc,
             Brand AS br,
             Trade AS tr
        LEFT JOIN PureSupplierSearchRequest AS pssr
        ON pssr.query_uuid = psr.query_uuid
        WHERE pssr.query_uuid = qpr.query_uuid
        AND psr.request_info.sales_channel_id = sc.sales_channel_id
        AND psr.request_info.brand_id = br.brand_id
        AND psr.request_info.trade_id = tr.trade_id""")
      .createOrReplaceTempView("RichSupplierSearchRequest")

    // SUPPLIER SEARCH REQUEST GRAPH
    val supplierGraph = spark.sql("""
      SELECT
         time,
         source,
         brand_name,
         trade_name,
         trade_group,
         trade_parent_group,
         xml_booking_login,
         count(query_uuid) as client_search_number,
         min((unix_timestamp(end_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.sss') - unix_timestamp(start_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.sss'))) as min_response_time,
         max((unix_timestamp(end_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.sss') - unix_timestamp(start_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.sss'))) as max_response_time,
         avg((unix_timestamp(end_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.sss') - unix_timestamp(start_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.sss'))) as avg_response_time,
         sum(timeout) as search_timeout_number,
         min(property_count) as min_property_number,
         avg(property_count) as avg_property_number,
         max(property_count) as max_property_number,
         count(success) as success_number,
         count(if(error_message is null,0,1)) as error_number,
         sum(request_count) as search_number
      FROM RichSupplierSearchRequest
      GROUP BY
         time,
         source,
         brand_name,
         trade_name,
         trade_group,
         trade_parent_group,
         xml_booking_login""")
      .na.fill("stub", Seq("time","brand_name", "sales_channel", "trade_parent_group", "trade_name", "trade_group", "xml_booking_login"))
      .as[SupplierSearchRequestInfo]





    // SAVING TO INFLUXDB
    supplierGraph.rdd.map { src =>
      com.pygmalios.reactiveinflux.Point(
        time = DateTime.now(),
        measurement = "supplier_search_request",
        tags = Map(
          "brand_name" -> Try(src.brand_name).getOrElse("no_brand")
          , "source" -> Try(src.source).getOrElse("no_source")
          , "trade_group" -> Try(src.trade_group).getOrElse("no_trade_group")
          , "trade_parent_group" -> Try(src.trade_parent_group).getOrElse("no_trade_parent_group")
          , "trade_name" -> Try(src.trade_name).getOrElse("no_trade_name")
          , "xml_booking_login" -> Try(src.xml_booking_login).getOrElse("no_xml")
        ),
        fields = Map(
          "search_count" -> Try(src.search_number.toInt).getOrElse(1)
          , "error_count" -> Try(src.error_number.toInt).getOrElse(1)
          , "success_count" -> Try(src.success_number.toInt).getOrElse(1)
          , "max_property_сount" -> Try(src.max_property_number.toInt).getOrElse(1)
          , "min_property_сount" -> Try(src.min_property_number.toInt).getOrElse(1)
          , "search_timeout_count" -> Try(src.search_timeout_number.toInt).getOrElse(1)
          , "avg_response_time" -> Try(src.avg_response_time).getOrElse(1.0)
          , "avg_property_number" -> Try(src.avg_property_number).getOrElse(1.0)
          , "max_response_time" -> Try(src.max_response_time.toInt).getOrElse(1)
          , "min_response_time" -> Try(src.min_response_time.toInt).getOrElse(1)
          , "search_timeout_count" -> Try(src.search_timeout_number.toInt).getOrElse(1)
        )
      )
    }.saveToInflux()
    // SAVING SUPPLIER GRAPH TO INFLUXDB
//    supplierGraph.foreachPartition { partition =>
//
//      // Open connection to Influxdb
//      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)
//
//      partition
//        .map(toPoint)
//        .foreach(p => Await.result(db.write(p), influxTimeout))
//
//      // Close connection
//      db.close()
//    }

//    spark.stop()
  }
}
