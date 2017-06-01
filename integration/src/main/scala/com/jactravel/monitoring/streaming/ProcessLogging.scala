package com.jactravel.monitoring.streaming

import com.jactravel.monitoring.model.ClientSearch
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.rabbitmq.RabbitMQUtils
;
/**
  * Created by eugene on 5/30/17.
  */

object ProcessLogging extends LazyLogging with ConfigService with ProcessMonitoringStream {

  def main(args: Array[String]): Unit = {

    import com.datastax.spark.connector._

    ssc = new StreamingContext(conf, Seconds(1))

    logger.info("-----------------------------------------------------------------------------------")
    logger.info("-----------------------------------------------------------------------------------")
    logger.info(s"hosts: $hosts")
    logger.info("-----------------------------------------------------------------------------------")
    logger.info("-----------------------------------------------------------------------------------")
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

//    receiverStream.foreachRDD(p => p.foreach(_.SearchQueryUUID))
    receiverStream.foreachRDD(_.saveToCassandra(keyspaceName, tableName))

    // Start the computation
    ssc.start()

    // Termination
    ssc.awaitTermination()
    receiverStream.stop()

  }
}