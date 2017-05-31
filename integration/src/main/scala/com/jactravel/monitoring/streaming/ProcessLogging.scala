package com.jactravel.monitoring.streaming

import com.jactravel.monitoring.model.ClientSearch
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.streaming.rabbitmq.RabbitMQUtils
;
/**
  * Created by eugene on 5/30/17.
  */

object ProcessLogging extends LazyLogging with ConfigRabbitmqData with ProcessMonitoringStream {

  def main(args: Array[String]): Unit = {
    val receiverStream = RabbitMQUtils.createStream[ClientSearch](ssc, Map(
      "hosts" -> hosts
      , "queueName" -> queueName
      , "exchangeName" -> exchangeName
      , "exchangeType" -> exchangeType
      , "vHost" -> vHost
      , "userName" -> username
      , "password" -> password
    )
    , messageHandler)

    // Start up the receiver.
    receiverStream.start()

    // Start the computation
    ssc.start()

    // Termination
    ssc.awaitTermination()
    receiverStream.stop()

  }
}