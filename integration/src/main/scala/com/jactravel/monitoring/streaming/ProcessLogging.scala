package com.jactravel.monitoring.streaming

import com.jactravel.monitoring.model._
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.rabbitmq.RabbitMQUtils
import org.apache.spark.streaming.{Duration, Milliseconds, Seconds, StreamingContext}

/**
  * Created by eugene on 5/30/17.
  */

object ProcessLogging extends LazyLogging with ConfigService with ProcessMonitoringStream {

  override val keyspaceName: String = "jactravel_monitoring_new"

  def main(args: Array[String]): Unit = {

    import com.datastax.spark.connector._
    import com.datastax.spark.connector.streaming._


    ssc = new StreamingContext(conf, Milliseconds(10000))

    val bookingStream = RabbitMQUtils.createStream[BookRequest](ssc
      , prepareQueueMap("BookRequest")
      , messageBookingHandler)

    val preBookingStream = RabbitMQUtils.createStream[PreBookRequest](ssc
      , prepareQueueMap("PreBookRequest")
      , messagePreBookingHandler)

    val searchRequestStream = RabbitMQUtils.createStream[SearchRequest](ssc
      , prepareQueueMap("SearchRequest")
      , messageSearchRequestHandler)

    val supplierBookhRequestStream = RabbitMQUtils.createStream[SupplierBookRequest](ssc
      , prepareQueueMap("SupplierBookRequest")
      , messageSupplierBookRequestHandler)

    val supplierPreBookRequestStream = RabbitMQUtils.createStream[SupplierPreBookRequest](ssc
      , prepareQueueMap("SupplierPreBookRequest")
      , messageSupplierPreBookRequestHandler)

    val supplierSearchRequestStream = RabbitMQUtils.createStream[SupplierSearchRequest](ssc
      , prepareQueueMap("SupplierSearchRequest")
      , messageSupplierSearchRequestHandler)
//
    val queryProxyStream = RabbitMQUtils.createStream[QueryProxyRequest](ssc
      , prepareQueueMap("QueryProxyRequest")
      , messageQueryProxyHandler)

    val cmiRequestStream = RabbitMQUtils.createStream[CmiRequest](ssc
      , prepareQueueMap("CMIRequest")
      , messageCmiRequestHandler)

    val cmiBatchRequestStream = RabbitMQUtils.createStream[CmiBatchRequest](ssc
      , prepareQueueMap("CMIBatchRequest")
      , messageCmiBatchRequestHandler)

  val numPar=100
    // Start up the receiver.
    bookingStream.repartition(numPar).saveToCassandra(keyspaceName, "book_request")
    preBookingStream.repartition(numPar).saveToCassandra(keyspaceName, "pre_book_request")
    searchRequestStream.repartition(numPar).saveToCassandra(keyspaceName, "search_request")
    supplierBookhRequestStream.repartition(numPar).saveToCassandra(keyspaceName, "supplier_book_request")
    supplierPreBookRequestStream.repartition(numPar).saveToCassandra(keyspaceName, "supplier_pre_book_request")
    supplierSearchRequestStream.repartition(numPar).saveToCassandra(keyspaceName, "supplier_search_request")
    queryProxyStream.repartition(numPar).saveToCassandra(keyspaceName, "query_proxy_request")
    cmiRequestStream.repartition(numPar).saveToCassandra(keyspaceName, "cmi_request")
    cmiBatchRequestStream.repartition(numPar).saveToCassandra(keyspaceName, "cmi_batch_request")
    bookingStream.map(br=>println("++++++++++++++++++++++++"+br.queryUUID))
    // Store query uuid
    bookingStream.map(br => QueryUUIDProceed(queryUUID = br.queryUUID))
      .repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")
    preBookingStream.map(br => QueryUUIDProceed(queryUUID = br.queryUUID))
      .repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")
    searchRequestStream.map { br =>QueryUUIDProceed(queryUUID = br.queryUUID)
    }.repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")
//    supplierBookRequestStream.map(br => QueryUUIDProceed(queryUUID = br.queryUUID))
//      .repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")
    supplierPreBookRequestStream.map(br => QueryUUIDProceed(queryUUID = br.queryUUID))
      .repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")
    supplierSearchRequestStream.map(br => QueryUUIDProceed(queryUUID = br.queryUUID))
      .repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")
    queryProxyStream.map(br => QueryUUIDProceed(queryUUID = br.queryUUID))
      .repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")
    cmiRequestStream.map(br => QueryUUIDProceed(queryUUID = br.queryUUID))
      .repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")
    cmiBatchRequestStream.map(br => QueryUUIDProceed(queryUUID = br.queryUUID))
      .repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")


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
      ,"maxMessagesPerPartition" -> "100"
      ,"levelParallelism"->"100"
      ,"rememberDuration" -> "1800000"
      ,"maxReceiveTime"->"500"
      ,"storageLevel"->"MEMORY_AND_DISK_2"

    )
  }
}