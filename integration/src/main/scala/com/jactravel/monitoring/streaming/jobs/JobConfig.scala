package com.jactravel.monitoring.streaming.jobs

import com.datastax.spark.connector.cql.CassandraConnectorConf
import com.datastax.spark.connector.rdd.ReadConf
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.cassandra._
import org.joda.time.{DateTime, DateTimeZone}

import scala.concurrent.duration._
import scala.util.Try

/**
  * Created by fayaz on 10.07.17.
  */
private[jobs] class JobConfig(appName: String) {

  private lazy val config : Config = ConfigFactory.load("reference.conf")

  val aws = Try(config.getString("aws")).getOrElse("")
  val dbServer = Try(config.getString("db.server")).getOrElse("34.230.10.7")
  val dbUseraname = Try(config.getString("db.username")).getOrElse("cassandra")
  val dbPassword = Try(config.getString("db.password")).getOrElse("xs9Zr6hfogrx")

  val influxHost = Try(config.getString("influxdb.host")).getOrElse("52.87.0.147")
  val influxPort = Try(config.getInt("influxdb.port")).getOrElse(8086)
  val influxDBname = Try(config.getString("influxdb.db")).getOrElse("my_db")

  // Date query
  val lower = DateTime.now(DateTimeZone.UTC).minusMinutes(10080).getMillis / 1000
  val upper = DateTime.now(DateTimeZone.UTC).minusMinutes(10000).getMillis / 1000
  val range = lower to upper by 1
  val in_condition = s"(${range.mkString(",")})"
  val query = s"query_second in $in_condition"

  // Await timeout
  val influxTimeout = 3 second

  // Spark conf
  val sparkConf = new SparkConf()
    .setAppName(appName)
    .setIfMissing("spark.master", "local[*]")//"spark://52.202.173.248:7077")
    .setIfMissing("spark.cassandra.connection.host", dbServer)
    .set("spark.cassandra.auth.username", dbUseraname)
    .set("spark.cassandra.auth.password", dbPassword)

  // Spark Session
  val spark = SparkSession
    .builder()
    .config(sparkConf)
    .getOrCreate()
    .setCassandraConf(CassandraConnectorConf.KeepAliveMillisParam.option(10000))
    .setCassandraConf("Cluster1", "ks1", ReadConf.SplitSizeInMBParam.option(128))
}
