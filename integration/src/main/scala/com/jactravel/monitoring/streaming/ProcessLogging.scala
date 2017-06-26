package com.jactravel.monitoring.streaming

import com.jactravel.monitoring.model._
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.streaming.rabbitmq.RabbitMQUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by eugene on 5/30/17.
  */

object ProcessLogging extends LazyLogging with ConfigService with ProcessMonitoringStream {

  override val keyspaceName: String = "jactravel_monitoring"

  def main(args: Array[String]): Unit = {

    import com.datastax.spark.connector._

    ssc = new StreamingContext(conf, Seconds(1))

    val bookingStream = RabbitMQUtils.createStream[BookRequest](ssc
      , prepareQueueMap("BookRequest")
      , messageBookingHandler)

    val preBookingStream = RabbitMQUtils.createStream[PreBookRequest](ssc
      , prepareQueueMap("PreBookRequest")
      , messagePreBookingHandler)

    val queryProxyStream = RabbitMQUtils.createStream[QueryProxyRequest](ssc
      , prepareQueueMap("QueryProxyRequest")
      , messageQueryProxyHandler)

    val searchRequestStream = RabbitMQUtils.createStream[(SearchRequestInfo, SearchResponseInfo)](ssc
      , prepareQueueMap("SearchRequest")
      , messageSearchReportHandler)

    val supplierBookhRequestStream = RabbitMQUtils.createStream[SupplierBookRequest](ssc
      , prepareQueueMap("SupplierBookRequest")
      , messageSupplierBookRequestHandler)

    val supplierPreBookRequestStream = RabbitMQUtils.createStream[SupplierPreBookRequest](ssc
      , prepareQueueMap("SupplierPreBookRequest")
      , messageSupplierPreBookRequestHandler)

    val supplierSearchRequestStream = RabbitMQUtils.createStream[SupplierSearchRequest](ssc
      , prepareQueueMap("SupplierSearchRequest")
      , messageSupplierSearchRequestHandler)


    // Start up the receiver.
    bookingStream.foreachRDD(_.saveToCassandra(keyspaceName, "book_request"))
    preBookingStream.foreachRDD(_.saveToCassandra(keyspaceName, "pre_book_request"))
    queryProxyStream.foreachRDD(_.saveToCassandra(keyspaceName, "query_proxy_request"))
    searchRequestStream.foreachRDD {
      rdd =>
        rdd.map(_._1).saveToCassandra(keyspaceName, "search_request_info")
        rdd.map(_._2).saveToCassandra(keyspaceName, "search_response_info")
    }
    supplierBookhRequestStream.foreachRDD(_.saveToCassandra(keyspaceName, "supplier_book_request"))
    supplierPreBookRequestStream.foreachRDD(_.saveToCassandra(keyspaceName, "supplier_pre_book_request"))
    supplierSearchRequestStream.foreachRDD(_.saveToCassandra(keyspaceName, "supplier_search_request"))

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