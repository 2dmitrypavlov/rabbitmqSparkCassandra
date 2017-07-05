package com.jactravel.monitoring.streaming

import com.jactravel.monitoring.model.influx.BookRequestInflux.BookRequestCount
import com.paulgoldbaum.influxdbclient.{InfluxDB, Point}
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Created by eugene on 6/26/17.
  */

object ProceedToInflux extends LazyLogging with ConfigService with ProcessMonitoringStream {

  def main(args: Array[String]): Unit = {

    conf.set("spark.cassandra.connection.keep_alive_ms", "60000")

    val spark =  SparkSession
      .builder()
      .config(conf)
      .getOrCreate()

    import spark.implicits._

    val brand = spark
      .read
      .format("com.databricks.spark.csv")
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load(aws + "brand.csv")

    val trade = spark
      .read
      .format("com.databricks.spark.csv")
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load(aws + "trade.csv")

    val salesChannel = spark
      .read
      .format("com.databricks.spark.csv")
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load(aws + "saleschannel.csv")

    val queryProxyRequest = spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map("table" -> "query_proxy_request", "keyspace" -> "jactravel_monitoring_new"))
      .load()

    val book_request = spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map("table" -> "book_request", "keyspace" -> "jactravel_monitoring_new"))
      .load()

    brand.createOrReplaceTempView("Brand")
    trade.createOrReplaceTempView("Trade")
    salesChannel.createOrReplaceTempView("SalesChannel")
    queryProxyRequest.createOrReplaceTempView("QueryProxyRequest")
    book_request.createOrReplaceTempView("BookRequest")

    spark.sql("resources/sql/booking/RichBooking.sql").createOrReplaceTempView("RichBooking")

    spark.sql("resources/sql/booking/BookingCount.sql").as[BookRequestCount]
      .foreachPartition { partition =>

        // Open connection to Influxdb
        val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)

        partition
          .map(toPoint)
          .foreach(p => Await.result(db.write(p), 1 seconds))

        // Close connection
        db.close()
      }

   def toPoint(brq: BookRequestCount): Point = {
      Point("booking_count")
        .addTag("tm", brq.tm.toString)
        .addTag("brand_name", brq.brand_name)
        .addTag("sales_channel", brq.sales_channel)
        .addTag("trade_group", brq.trade_group)
        .addTag("trade_name", brq.trade_name)
        .addTag("trade_parent_group", brq.trade_parent_group)
        .addTag("xml_booking_login", brq.xml_booking_login)
        .addField("booking_count", brq.booking_count)
   }

//    val queryUuidRdd = ssc.sparkContext
//      .cassandraTable(keyspaceName, "query_uuid_proceed")
//      .select("query_uuid").where("proceed < ?", 1)
//      .limit(50) //.keyBy("query_uuid")
//
//    queryUuidRdd.cache()
//
//    val queryProxyRequest = queryUuidRdd
//      .repartitionByCassandraReplica(keyspaceName, "query_proxy_request")
//      .joinWithCassandraTable[QueryProxyRequest](keyspaceName, "query_proxy_request")
//
//    queryProxyRequest.cache()
//
//    val streamSearchRequest = queryUuidRdd
//      .repartitionByCassandraReplica(keyspaceName, "search_request")
//      .joinWithCassandraTable[SearchRequest](keyspaceName, "search_request")
//      .map {
//        sr =>
//          RichSearchRequest(
//            queryUUID = sr._2.queryUUID
//            , brand_id = sr._2.requestInfo.brandId
//            , trade_id = sr._2.requestInfo.tradeId
//            , sales_channel_id = sr._2.requestInfo.salesChannelId
//            , responseTimeMillis = DateTimeUtils.datesDiff(sr._2.requestInfo.endUtcTimestamp, sr._2.requestInfo.startUtcTimestamp)
//          )
//      }
//
//    streamSearchRequest.cache()
//
//    val streamSearchRequestWQuery = streamSearchRequest
//      .repartitionByCassandraReplica(keyspaceName, "query_proxy_request")
//      .leftJoinWithCassandraTable[QueryProxyRequest](keyspaceName, "query_proxy_request")
//      .map {
//        case (searchRequest, optQueryProxyRequest) =>
//          optQueryProxyRequest.map(queryProxyRequest => searchRequest.copy(
//            xmlBookingLogin = queryProxyRequest.xmlBookingLogin
//          )).getOrElse(searchRequest)
//      }
//
////    streamSearchRequestWQuery.cache()
//
//    val searchRequestTrade = streamSearchRequestWQuery
//      .leftJoinWithCassandraTable[Trade](keyspaceName, "trade")
//      .map {
//        case (searchRequest, optTrade) =>
//          optTrade.map(trade => searchRequest.copy(
//            tradeName = trade.tradeName.getOrElse("")
//            , tradeGroup = trade.tradeGroup.getOrElse("")
//            , traderParentGroup = trade.tradeGroup.getOrElse("")
//          )).getOrElse(searchRequest)
//      }
//
//    val searchRequestBrand = searchRequestTrade
//      .leftJoinWithCassandraTable[Brand](keyspaceName, "brand")
//      .map {
//        case (searchRequest, optBrand) =>
//          optBrand.map(brand => searchRequest.copy(
//            brandName = brand.brandName
//          )).getOrElse(searchRequest)
//      }
//
//    val searchRequestSalesChannel = searchRequestTrade
//      .leftJoinWithCassandraTable[SalesChannel](keyspaceName, "sales_channel")
//      .map {
//        case (searchRequest, optSalesChannel) =>
//          optSalesChannel.map(salesChannel => searchRequest.copy(
//            salesChannel = salesChannel.salesChannel
//          )).getOrElse(searchRequest)
//      }
//
////    val dstream = new ConstantInputDStream(ssc, searchRequestSalesChannel)
////
////    dstream.foreachRDD { rdd =>
////      // any action will trigger the underlying cassandra query, using collect to have a simple output
////      try {
////        println("======================================================")
////        println(s"--------------------------------- ${rdd.collect.mkString("\n")}")
////        println("======================================================")
////      } catch {
////        case e: Exception => e.printStackTrace
////      }
////    }
//    searchRequestSalesChannel.foreachPartition { partition =>
//      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)
//
//      partition
//        .map(toPoint)
//        .foreach(p => Await.result(db.write(p), 1 seconds))
//
//      db.close()
//    }
//
//  def toPoint(rsr: RichSearchRequest): Point = {
//    //todo: expand to original model
//    Point("search_request")
//      .addTag("queryUUID", rsr.queryUUID)
//      .addField("brand_id", rsr.brand_id)
//  }
//
////    queryUUID: String
////    , brand_id: Int = -1
////    , brandName: String = ""
////    , trade_id: Int = -1
////    , tradeName: String = ""
////    , tradeGroup: String = ""
////    , traderParentGroup: String = ""
////    , sales_channel_id: Int = -1
////    , salesChannel: String = ""
////    , responseTimeMillis: Long = 0L
////    , errorStackTrace: String = ""
////    , success: String = ""
////    , xmlBookingLogin: String = ""
////    , requestTime: LocalDateTime = null
//
//    ssc.sparkContext.stop()
//    ssc.checkpoint(System.getProperty("java.io.tmpdir"))
//    // Start the computation
//    ssc.start()
//
//    // Termination
//    ssc.awaitTermination()

  }

  private[this] def prepareQueueMap(queueName: String) = {
    Map(
      "hosts" -> hosts
      , "queueName" -> queueName
      , "exchangeName" -> exchangeName
      , "exchangeType" -> exchangeType
      , "vHost" -> vHost
      , "userName" -> username
      , "password" -> password
    )
  }
}