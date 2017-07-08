package com.jactravel.monitoring.streaming

import com.jactravel.monitoring.model._
import com.jactravel.monitoring.model.tmp.{BookRequestTime, PreBookRequestTime, QueryProxyRequestTime, SearchRequestTime}
import com.jactravel.monitoring.streaming.ProceedToInflux.{conf, keyspaceName, ssc}
import com.jactravel.monitoring.streaming.ProcessBusiness.conf
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.ConstantInputDStream
import org.apache.spark.streaming.rabbitmq.RabbitMQUtils
import org.apache.spark.streaming.{Minutes, StreamingContext}
import org.joda.time.{DateTime, DateTimeZone}

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by eugene on 5/30/17.
  */

object TradeSearchArchiving extends LazyLogging with ConfigService with ProcessMonitoringStream {

  def main(args: Array[String]): Unit = {

//    import com.datastax.spark.connector.streaming._

    val conf = new SparkConf()
      .setAppName("search_request_archiving")
      .setIfMissing("spark.master", "local[*]")

    conf.setIfMissing("spark.cassandra.connection.host", dbServer)
    conf.set("spark.cassandra.auth.username", dbUseraname)
    conf.set("spark.cassandra.auth.password", dbPassword)
    conf.set("spark.cassandra.connection.keep_alive_ms", "60000")

    val spark = SparkSession.builder().config(conf).getOrCreate()
    spark.sparkContext.setLogLevel("ERROR")
    //ssc = new StreamingContext(spark.sparkContext, Minutes(1))

    import spark.implicits._

    val lower = DateTime.now().withZone(DateTimeZone.UTC).minusHours(14).minusMinutes(35).getMillis / 1000
    val upper = DateTime.now().withZone(DateTimeZone.UTC).minusHours(14).minusMinutes(30).getMillis / 1000

    val range = lower to upper by 1

    val dfSearchRequest = spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map( "table" -> "search_request_second", "keyspace" -> keyspaceName))
      .load

//    dfSearchRequest.

    val dfQueryProxRequest = spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map( "table" -> "query_proxy_request_second", "keyspace" -> keyspaceName))
      .load


    val dfSearchRequestFiltered = dfSearchRequest
      .where($"query_second".isin(range.toList:_*))
    val dfQueryProxyRequestRenamed = dfQueryProxRequest
      .withColumnRenamed("query_second", "query_proxy_second")
      .where($"query_proxy_second".isin(range.toList:_*))
    val dfFiltered = dfSearchRequestFiltered
      .join(dfQueryProxyRequestRenamed, dfSearchRequestFiltered("query_uuid") === dfQueryProxyRequestRenamed("query_uuid"), "left_outer")


    println(s"Count: ${dfFiltered.count()} ")

    dfFiltered.printSchema()

//    val searchRequestStream = ssc
//      .cassandraTable[SearchRequest2](keyspaceName, "search_request_second")
//      .where("query_second in (?)", range.mkString(","))
//
//    val dstream = new ConstantInputDStream(ssc, searchRequestStream)
//
//    dstream.foreachRDD {
//      rdd => rdd.to
//    }

    // Start the computation
//    ssc.start()
//
//    // Termination
//    ssc.awaitTermination()

  }
}