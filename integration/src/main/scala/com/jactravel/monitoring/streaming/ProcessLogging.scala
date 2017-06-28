package com.jactravel.monitoring.streaming

import com.jactravel.monitoring.model._
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.streaming.rabbitmq.RabbitMQUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by eugene on 5/30/17.
  */

object ProcessLogging extends LazyLogging with ConfigService with ProcessMonitoringStream {

  override val keyspaceName: String = "jactravel_monitoring_new"

  def main(args: Array[String]): Unit = {

    import com.datastax.spark.connector._
    import com.datastax.spark.connector.streaming._

    ssc = new StreamingContext(conf, Seconds(1))

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

    val queryProxyStream = RabbitMQUtils.createStream[QueryProxyRequest](ssc
      , prepareQueueMap("QueryProxyRequest")
      , messageQueryProxyHandler)

    val cmiRequestStream = RabbitMQUtils.createStream[CmiRequest](ssc
      , prepareQueueMap("CMIRequest")
      , messageCmiRequestHandler)

    val cmiBatchRequestStream = RabbitMQUtils.createStream[CmiBatchRequest](ssc
      , prepareQueueMap("CMIBatchRequest")
      , messageCmiBatchRequestHandler)


    // Start up the receiver.
    bookingStream.saveToCassandra(keyspaceName, "book_request")
    preBookingStream.saveToCassandra(keyspaceName, "pre_book_request")
    searchRequestStream.saveToCassandra(keyspaceName, "search_request")
    supplierBookhRequestStream.saveToCassandra(keyspaceName, "supplier_book_request")
    supplierPreBookRequestStream.saveToCassandra(keyspaceName, "supplier_pre_book_request")
    supplierSearchRequestStream.saveToCassandra(keyspaceName, "supplier_search_request")
    queryProxyStream.saveToCassandra(keyspaceName, "query_proxy_request")
    cmiRequestStream.saveToCassandra(keyspaceName, "cmi_request")
    cmiBatchRequestStream.saveToCassandra(keyspaceName, "cmi_batch_request")

    // Store query uuid
    bookingStream.map(br => QueryUUID(queryUUID = br.queryUUID)).saveToCassandra(keyspaceName, "query_uuid")
    preBookingStream.map(br => QueryUUID(queryUUID = br.queryUUID)).saveToCassandra(keyspaceName, "query_uuid")
    searchRequestStream.map(br => QueryUUID(queryUUID = br.queryUUID)).saveToCassandra(keyspaceName, "query_uuid")
    supplierBookhRequestStream.map(br => QueryUUID(queryUUID = br.queryUUID)).saveToCassandra(keyspaceName, "query_uuid")
    supplierPreBookRequestStream.map(br => QueryUUID(queryUUID = br.queryUUID)).saveToCassandra(keyspaceName, "query_uuid")
    supplierSearchRequestStream.map(br => QueryUUID(queryUUID = br.queryUUID)).saveToCassandra(keyspaceName, "query_uuid")
    queryProxyStream.map(br => QueryUUID(queryUUID = br.queryUUID)).saveToCassandra(keyspaceName, "query_uuid")
    cmiRequestStream.map(br => QueryUUID(queryUUID = br.queryUUID)).saveToCassandra(keyspaceName, "query_uuid")
    cmiBatchRequestStream.map(br => QueryUUID(queryUUID = br.queryUUID)).saveToCassandra(keyspaceName, "query_uuid")


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