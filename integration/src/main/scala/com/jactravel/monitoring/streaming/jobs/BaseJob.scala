package com.jactravel.monitoring.streaming.jobs

import com.datastax.spark.connector.cql.CassandraConnectorConf
import com.datastax.spark.connector.rdd.ReadConf
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.cassandra._
import org.joda.time.{DateTime, DateTimeZone}

import scala.concurrent.duration._

/**
  * Created by fayaz on 09.07.17.
  */
trait BaseJob {

  //todo: delete or expand in future
  // Date query
  val lower = DateTime.now(DateTimeZone.UTC).minusMinutes(10).getMillis / 1000
  val upper = DateTime.now(DateTimeZone.UTC).minusMinutes(5).getMillis / 1000
  val range = lower to upper by 1
  val in_condition = s"(${range.mkString(",")})"
  val query = s"query_second in $in_condition"

  // Await timeout
  val influxTimeout = 1 second

  // Spark conf
  val sparkConf = new SparkConf()
    .setAppName("request-job")
    .setIfMissing("spark.master", "local[*]")//"spark://52.202.173.248:7077")

  // Spark Session
  val spark = SparkSession
    .builder()
    .config(sparkConf)
    .getOrCreate()
    .setCassandraConf(CassandraConnectorConf.KeepAliveMillisParam.option(10000))
    .setCassandraConf("Cluster1", "ks1", ReadConf.SplitSizeInMBParam.option(128))

}
