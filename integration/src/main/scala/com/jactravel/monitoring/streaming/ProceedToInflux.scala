package com.jactravel.monitoring.streaming

import com.jactravel.monitoring.model._
import com.jactravel.monitoring.model.influx.RichSearchRequest
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.streaming.dstream.ConstantInputDStream
import org.apache.spark.streaming.rabbitmq.RabbitMQUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by eugene on 6/26/17.
  */

object ProceedToInflux extends LazyLogging with ConfigService with ProcessMonitoringStream {

  override val keyspaceName: String = "jactravel_monitoring"

  def main(args: Array[String]): Unit = {

    import com.datastax.spark.connector._
    import com.datastax.spark.connector.streaming._

    ssc = new StreamingContext(conf, Seconds(5))


    // Start up the receiver.


    val cassandraRDD = ssc.cassandraTable(keyspaceName, "query_uuid_proceed")
      .select("query_uuid", "proceed").where("proceed < ?", 1).limit(250).keyBy("query_uuid")

    val dstream = new ConstantInputDStream(ssc, cassandraRDD)

    val streamSearchRequest = dstream
      .joinWithCassandraTable[SearchRequest](keyspaceName, "search_request")
      .map {
        sr =>
          RichSearchRequest(queryUUID = sr._2.queryUUID, tradeId = sr._2.requestInfo.tradeId)
      }
    //val r = streamSearchRequest.joinWithCassandraTable(keyspaceName, "trade", SomeColumns("trade_id"), SomeColumns("trade_id")).map(o => (o._1, o._2.columnValues.toList))

    streamSearchRequest.foreachRDD { rdd =>
      // any action will trigger the underlying cassandra query, using collect to have a simple output
      try {
        println("======================================================")
        println(s"--------------------------------- ${rdd.collect.mkString("\n")}")
        println("======================================================")
      } catch {
        case e: Exception => e.printStackTrace
      }
    }

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