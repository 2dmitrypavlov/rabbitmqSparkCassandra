package com.jactravel.monitoring.streaming

import com.jactravel.monitoring.model.ClientSearch
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.SparkContext
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.rabbitmq.RabbitMQUtils
;
/**
  * Created by eugene on 5/30/17.
  */

object ProcessLogging extends LazyLogging with ConfigRabbitmqData with ProcessMonitoringStream {

  //import com.datastax.bdp.spark.writer.BulkTableWriter._
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

    sc = new SparkContext(conf)
    ssc = new StreamingContext(sc, Seconds(1))

    // Start up the receiver.
    receiverStream.start()

    //receiverStream.foreachRDD(rdd => rdd.saveAsObjectFile())

    // Start the computation
    ssc.start()

    // Termination
    ssc.awaitTermination()
    receiverStream.stop()

  }
}