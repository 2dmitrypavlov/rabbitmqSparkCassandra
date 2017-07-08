package com.jactravel.monitoring.streaming

import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

import com.datastax.spark.connector.writer.{TTLOption, WriteConf}
import com.jactravel.monitoring.model._
//import com.jactravel.monitoring.model.Ca
import com.jactravel.monitoring.model.tmp.{BookRequestTime, PreBookRequestTime, QueryProxyRequestTime, SearchRequestTime}
import com.jactravel.monitoring.util.DateTimeUtils
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.rabbitmq.RabbitMQUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by eugene on 5/30/17.
  */

object ProcessLogging extends LazyLogging with ConfigService with ProcessMonitoringStream {

  //  override val keyspaceName: String = "jactravel_monitoring"

  def main(args: Array[String]): Unit = {

    import com.datastax.spark.connector.streaming._

    val spark = SparkSession
      .builder()
      .config(conf)
      .getOrCreate()

    ssc = new StreamingContext(spark.sparkContext, Seconds(1))

    import spark.implicits._

    val brands = spark.read
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .csv(aws + "brand.csv")
      .as[Brand]
      .map(b => b.brand_id -> b.brand_name)
      .collect().toMap

    val trades = spark
      .read
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .csv(aws + "trade.csv")
      .as[Trade]
      .map(t => t.trade_id -> t)
      .collect().toMap

//    val saleschannels = spark
//      .read
//      .option("header", "true") // Use first line of all files as header
//      .option("inferSchema", "true") // Automatically infer data types
//      .load(aws + "saleschannel.csv")
//      .as[SalesChannel]
//      .map(s => s.sales_channel_id -> s.sales_channel)
//      .collect().toMap


    val saleschannels = ssc
      .cassandraTable[SalesChannel](keyspaceName, "sales_channel")
      .map(s => s.sales_channel_id -> s.sales_channel)
      .collect().toMap

    val bookingStream = RabbitMQUtils.createStream[BookRequestTime](ssc
      , prepareQueueMap("BookRequest")
      , messageBookingTimeHandler)

    //    val preBookingStream = RabbitMQUtils.createStream[PreBookRequest](ssc
    //      , prepareQueueMap("PreBookRequest")
    //      , messagePreBookingHandler)

    val preBookingStream = RabbitMQUtils.createStream[PreBookRequestTime](ssc
      , prepareQueueMap("PreBookRequest")
      , messagePreBookingTimeHandler)

    //    val searchRequestStream = RabbitMQUtils.createStream[SearchRequest](ssc
    //      , prepareQueueMap("SearchRequest")
    //      , messageSearchRequestHandler)

    val searchRequestStream = RabbitMQUtils.createStream[SearchRequestTime](ssc
      , prepareQueueMap("SearchRequest")
      , messageSearchRequestTimeHandler)

    val supplierBookhRequestStream = RabbitMQUtils.createStream[SupplierBookRequest](ssc
      , prepareQueueMap("SupplierBookRequest")
      , messageSupplierBookRequestHandler)

    val supplierPreBookRequestStream = RabbitMQUtils.createStream[SupplierPreBookRequest](ssc
      , prepareQueueMap("SupplierPreBookRequest")
      , messageSupplierPreBookRequestHandler)

    val supplierSearchRequestStream = RabbitMQUtils.createStream[SupplierSearchRequest](ssc
      , prepareQueueMap("SupplierSearchRequest")
      , messageSupplierSearchRequestHandler)

    //    val queryProxyStream = RabbitMQUtils.createStream[QueryProxyRequest](ssc
    //      , prepareQueueMap("QueryProxyRequest")
    //      , messageQueryProxyHandler)

    val queryProxyStream = RabbitMQUtils.createStream[QueryProxyRequestTime](ssc
      , prepareQueueMap("QueryProxyRequest")
      , messageQueryProxyTimeHandler)

    val cmiRequestStream = RabbitMQUtils.createStream[CmiRequest](ssc
      , prepareQueueMap("CMIRequest")
      , messageCmiRequestHandler)

    val cmiBatchRequestStream = RabbitMQUtils.createStream[CmiBatchRequest](ssc
      , prepareQueueMap("CMIBatchRequest")
      , messageCmiBatchRequestHandler)

    val numPar = 200
    // Start up the receiver.
    queryProxyStream.repartition(numPar).saveToCassandra(keyspaceName, "query_proxy_request_time")
    searchRequestStream.repartition(numPar).saveToCassandra(keyspaceName, "search_request_time")
    bookingStream.repartition(numPar).saveToCassandra(keyspaceName, "book_request_time")
    preBookingStream.repartition(numPar).saveToCassandra(keyspaceName, "pre_book_request_time")
    supplierBookhRequestStream.repartition(numPar).saveToCassandra(keyspaceName, "supplier_book_request")
    supplierPreBookRequestStream.repartition(numPar).saveToCassandra(keyspaceName, "supplier_pre_book_request")
    supplierSearchRequestStream.repartition(numPar).saveToCassandra(keyspaceName, "supplier_search_request")
    cmiRequestStream.repartition(numPar).saveToCassandra(keyspaceName, "cmi_request")
    cmiBatchRequestStream.repartition(numPar).saveToCassandra(keyspaceName, "cmi_batch_request")

    // Store query uuid
    bookingStream
      .repartition(numPar)
      .map {
        data =>
          QueryProxyRequestExt(
            queryUuid = data.queryUUID,
            tableName = Some("book_request")
          )
      }
      .saveToCassandra(keyspaceName, "query_proxy_request_ext", writeConf = WriteConf(ttl = TTLOption.constant(15.minutes)))
    //    preBookingStream.map(br => QueryUUIDProceed(queryUUID = br.queryUUID))
    //      .repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")
    //    searchRequestStream.map { br => QueryUUIDProceed(queryUUID = br.queryUUID)
    //    }.repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")
    //    //    supplierBookRequestStream.map(br => QueryUUIDProceed(queryUUID = br.queryUUID))
    //    //      .repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")
    //    supplierPreBookRequestStream.map(br => QueryUUIDProceed(queryUUID = br.queryUUID))
    //      .repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")
    //    supplierSearchRequestStream.map(br => QueryUUIDProceed(queryUUID = br.queryUUID))
    //      .repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")


    queryProxyStream.repartition(numPar)
      .map {
        data =>
          QueryProxyRequestExt(
            queryUuid = data.queryUUID
          )
      }
      .saveToCassandra(keyspaceName, "query_proxy_request_ext", writeConf = WriteConf(ttl = TTLOption.constant(15.minutes)))
    //    cmiRequestStream.map(br => QueryUUIDProceed(queryUUID = br.queryUUID))
    //      .repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")
    //    cmiBatchRequestStream.map(br => QueryUUIDProceed(queryUUID = br.queryUUID))
    //      .repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")


    // Start the computation
    ssc.start()

    // Termination
    ssc.awaitTermination()

  }

  private[this] def prepareQueueMap(queueName: String) = {
    Map(
      "hosts" -> hosts
      , "queueName" -> queueName
      //      , "exchangeName" -> exchangeName
      //      , "exchangeType" -> exchangeType
      , "vHost" -> vHost
      , "userName" -> username
      , "password" -> password
      , "routingKey" -> queueName
      // ,"maxMessagesPerPartition"->"1"
      , "maxMessagesPerPartition" -> "100"
      , "levelParallelism" -> "100"
      , "rememberDuration" -> "1800000"
      , "maxReceiveTime" -> "500"
      , "storageLevel" -> "MEMORY_AND_DISK_2"

    )
  }
}