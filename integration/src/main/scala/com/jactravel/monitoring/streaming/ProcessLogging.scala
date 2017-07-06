package com.jactravel.monitoring.streaming

import com.jactravel.monitoring.model._
import com.jactravel.monitoring.model.tmp.{BookRequestTime, PreBookRequestTime, QueryProxyRequestTime, SearchRequestTime}
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.streaming.rabbitmq.RabbitMQUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by eugene on 5/30/17.
  */

object ProcessLogging extends LazyLogging with ConfigService with ProcessMonitoringStream {

//  override val keyspaceName: String = "jactravel_monitoring"

  def main(args: Array[String]): Unit = {

    import com.datastax.spark.connector.streaming._

    ssc = new StreamingContext(conf, Seconds(1))

    //    val bookingStream = RabbitMQUtils.createStream[BookRequest](ssc
    //      , prepareQueueMap("BookRequest")
    //      , messageBookingHandler)
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
//    bookingStream.map(br => QueryUUIDProceed(queryUUID = br.queryUUID))
//      .repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")
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
//    queryProxyStream.map(br => QueryUUIDProceed(queryUUID = br.queryUUID))
//      .repartition(numPar).saveToCassandra(keyspaceName, "query_uuid_proceed")
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