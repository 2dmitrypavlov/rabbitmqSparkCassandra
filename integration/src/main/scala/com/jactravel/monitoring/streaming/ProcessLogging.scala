package com.jactravel.monitoring.streaming

import com.jactravel.monitoring.model._
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.streaming.rabbitmq.RabbitMQUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
;

/**
  * Created by eugene on 5/30/17.
  */

object ProcessLogging extends LazyLogging with ConfigService with ProcessMonitoringStream {

  override val keyspaceName: String = "jactravel_monitoring"

  def main(args: Array[String]): Unit = {

    import com.datastax.spark.connector._

    ssc = new StreamingContext(conf, Seconds(1))

    val bookingStream = RabbitMQUtils.createStream[BookRequest](ssc, Map(
      "hosts" -> hosts
      , "queueName" -> "BookRequest"
      , "exchangeName" -> exchangeName
      , "exchangeType" -> exchangeType
      , "vHost" -> vHost
      , "userName" -> username
      , "password" -> password
    )
      , messageBookingHandler)

    val preBookingStream = RabbitMQUtils.createStream[PreBookRequest](ssc, Map(
      "hosts" -> hosts
      , "queueName" -> "PreBookRequest"
      , "exchangeName" -> exchangeName
      , "exchangeType" -> exchangeType
      , "vHost" -> vHost
      , "userName" -> username
      , "password" -> password
    )
      , messagePreBookingHandler)

    val queryProxyStream = RabbitMQUtils.createStream[QueryProxyRequest](ssc, Map(
      "hosts" -> hosts
      , "queueName" -> "QueryProxyRequest"
      , "exchangeName" -> exchangeName
      , "exchangeType" -> exchangeType
      , "vHost" -> vHost
      , "userName" -> username
      , "password" -> password
    )
      , messageQueryProxyHandler)

    val searchRequestStream = RabbitMQUtils.createStream[(SearchRequestInfo, SearchResponseInfo)](ssc, Map(
      "hosts" -> hosts
      , "queueName" -> "SearchRequest"
      , "exchangeName" -> exchangeName
      , "exchangeType" -> exchangeType
      , "vHost" -> vHost
      , "userName" -> username
      , "password" -> password
    )
      , messageSearchRequestHandler)


    // Start up the receiver.
    bookingStream.start()
    preBookingStream.start()
    queryProxyStream.start()

    bookingStream.foreachRDD(_.saveToCassandra(keyspaceName, "book_request"))
    preBookingStream.foreachRDD(_.saveToCassandra(keyspaceName, "pre_book_request"))
    queryProxyStream.foreachRDD(_.saveToCassandra(keyspaceName, "query_proxy_request"))
    searchRequestStream.foreachRDD {
      rdd =>
        rdd.map(_._1).saveToCassandra(keyspaceName, "search_request_info")
        rdd.map(_._2).saveToCassandra(keyspaceName, "search_response_info")
    }

    // Start the computation
    ssc.start()

    // Termination
    ssc.awaitTermination()

    searchRequestStream.stop()
    queryProxyStream.stop()
    preBookingStream.stop()
    bookingStream.stop()

  }
}