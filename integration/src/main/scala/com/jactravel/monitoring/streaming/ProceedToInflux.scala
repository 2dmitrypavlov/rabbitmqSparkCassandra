package com.jactravel.monitoring.streaming

import java.time.temporal.ChronoUnit
import java.time.{ZoneId, ZoneOffset, ZonedDateTime}
import java.util.Date

import com.jactravel.monitoring.model._
import com.jactravel.monitoring.model.influx.RichSearchRequest
import com.jactravel.monitoring.model.tmp.SearchRequestTime
import com.jactravel.monitoring.util.DateTimeUtils
import org.apache.spark.streaming.Seconds
//import com.paulgoldbaum.influxdbclient.{InfluxDB, Point}
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.streaming.dstream.ConstantInputDStream
import org.apache.spark.streaming.{Milliseconds, Minutes, StreamingContext}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Created by eugene on 6/26/17.
  */

object ProceedToInflux extends LazyLogging with ConfigService with ProcessMonitoringStream {

  def main(args: Array[String]): Unit = {

    import com.datastax.spark.connector._

    conf.set("spark.cassandra.connection.keep_alive_ms", "60000")

    ssc = new StreamingContext(conf, Minutes(1))

    val startDateTime = Date.from(ZonedDateTime.now(ZoneOffset.UTC).minus(360, ChronoUnit.SECONDS).toInstant)
    val endDateTime = Date.from(ZonedDateTime.now(ZoneOffset.UTC).minus(60, ChronoUnit.SECONDS).toInstant)

    val searchRequestStream = ssc.sparkContext
      .cassandraTable[SearchRequestTime](keyspaceName, "search_request_time")
      .where("request_utc_timestamp > ? and request_utc_timestamp < ?",
        Date.from(ZonedDateTime.now(ZoneOffset.UTC).minus(360, ChronoUnit.SECONDS).toInstant),
        Date.from(ZonedDateTime.now(ZoneOffset.UTC).minus(60, ChronoUnit.SECONDS).toInstant))
//      .keyBy("query_uuid")

    searchRequestStream.cache()

    println("=========================================================================================")
    println(s"S: ${ZonedDateTime.now(ZoneOffset.UTC).minus(360, ChronoUnit.SECONDS).toString}  E: ${endDateTime.toString}")
    println(s"Count: ${searchRequestStream.count()}")
    println("-----------------------------------------------------------------------------------------")

    val queryProxyRequestStream = searchRequestStream
      .repartitionByCassandraReplica(keyspaceName, "query_proxy_request_time")
      .leftJoinWithCassandraTable[QueryProxyRequest](keyspaceName, "query_proxy_request_time")

    queryProxyRequestStream.cache()

    /*val streamSearchRequest = queryUuidRdd
      .repartitionByCassandraReplica(keyspaceName, "search_request")
      .joinWithCassandraTable[SearchRequest](keyspaceName, "search_request")
      .map {
        sr =>
          RichSearchRequest(
            queryUUID = sr._2.queryUUID
            , brand_id = sr._2.requestInfo.brandId
            , trade_id = sr._2.requestInfo.tradeId
            , sales_channel_id = sr._2.requestInfo.salesChannelId
            , responseTimeMillis = DateTimeUtils.datesDiff(sr._2.requestInfo.endUtcTimestamp, sr._2.requestInfo.startUtcTimestamp)
          )
      }

    streamSearchRequest.cache()

    val streamSearchRequestWQuery = streamSearchRequest
      .repartitionByCassandraReplica(keyspaceName, "query_proxy_request")
      .leftJoinWithCassandraTable[QueryProxyRequest](keyspaceName, "query_proxy_request")
      .map {
        case (searchRequest, optQueryProxyRequest) =>
          optQueryProxyRequest.map(queryProxyRequest => searchRequest.copy(
            xmlBookingLogin = queryProxyRequest.xmlBookingLogin
          )).getOrElse(searchRequest)
      }

//    streamSearchRequestWQuery.cache()

    val searchRequestTrade = streamSearchRequest
      .leftJoinWithCassandraTable[Trade](keyspaceName, "trade")
      .map {
        case (searchRequest, optTrade) =>
          optTrade.map(trade => searchRequest.copy(
            tradeName = trade.tradeName.getOrElse("")
            , tradeGroup = trade.tradeGroup.getOrElse("")
            , traderParentGroup = trade.tradeGroup.getOrElse("")
          )).getOrElse(searchRequest)
      }

    val searchRequestBrand = searchRequestTrade
      .leftJoinWithCassandraTable[Brand](keyspaceName, "brand")
      .map {
        case (searchRequest, optBrand) =>
          optBrand.map(brand => searchRequest.copy(
            brandName = brand.brandName
          )).getOrElse(searchRequest)
      }

    val searchRequestSalesChannel = searchRequestTrade
      .leftJoinWithCassandraTable[SalesChannel](keyspaceName, "sales_channel")
      .map {
        case (searchRequest, optSalesChannel) =>
          optSalesChannel.map(salesChannel => searchRequest.copy(
            salesChannel = salesChannel.salesChannel
          )).getOrElse(searchRequest)
      }
      */

    val dstream = new ConstantInputDStream(ssc, searchRequestStream)

    dstream.foreachRDD { rdd =>
      // any action will trigger the underlying cassandra query, using collect to have a simple output
      try {
        println("======================================================")
        println(s"++++++++++++++++++++++++++++++++++++++++++++++++++++++ ${rdd.collect.mkString("\n")}")
        println("======================================================")
      } catch {
        case e: Exception => e.printStackTrace()
      }
    }
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

//    queryUUID: String
//    , brand_id: Int = -1
//    , brandName: String = ""
//    , trade_id: Int = -1
//    , tradeName: String = ""
//    , tradeGroup: String = ""
//    , traderParentGroup: String = ""
//    , sales_channel_id: Int = -1
//    , salesChannel: String = ""
//    , responseTimeMillis: Long = 0L
//    , errorStackTrace: String = ""
//    , success: String = ""
//    , xmlBookingLogin: String = ""
//    , requestTime: LocalDateTime = null

//    ssc.sparkContext.stop()
    ssc.checkpoint(System.getProperty("java.io.tmpdir"))
    // Start the computation
    ssc.start()

    // Termination
    ssc.awaitTermination()

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