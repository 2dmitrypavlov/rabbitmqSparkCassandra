package com.jactravel.monitoring.streaming

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Try

/**
  * Created by eugene on 5/30/17.
  */
private[streaming] trait ConfigService {
  implicit val system = ActorSystem("ActorRabbitMQSystem")
  implicit val mat = ActorMaterializer()
  implicit val timeout = Timeout(10 seconds)

  private lazy val config : Config = ConfigFactory.load("reference.conf")

  val totalRegisters = 10000

  /**
    * Spark Properties
    */

  val conf = new SparkConf()
    .setAppName("logs-monitoring-receiver")
    .setIfMissing("spark.master", "local[*]")//"spark://52.202.173.248:7077")
  var ssc: StreamingContext = _

  /**
    * RabbitMQ Properties
    */
  //val queueName = Try(config.getString("amqp.queueName")).getOrElse("rabbitmq-queue")
  val aws = Try(config.getString("aws")).getOrElse("")
  val exchangeName = Try(config.getString("amqp.exchangeName")).getOrElse("")
  val exchangeType = Try(config.getString("amqp.exchangeType")).getOrElse("")
  val routingKey = Try(config.getString("rabbitmq.routingKey")).getOrElse("")
  val vHost = Try(config.getString("amqp.virtual-host")).getOrElse("/")
  val hosts = Try(config.getStringList("amqp.addresses.host").get(0)).getOrElse("ec2-34-225-142-10.compute-1.amazonaws.com")
  val username = Try(config.getString("amqp.username")).getOrElse("guest")
  val password = Try(config.getString("rabbitmq.password")).getOrElse("guest")
  val dbServer = Try(config.getString("db.server")).getOrElse("34.230.10.7")
  val dbUseraname = Try(config.getString("db.username")).getOrElse("cassandra")
  val dbPassword = Try(config.getString("db.password")).getOrElse("xs9Zr6hfogrx")

  conf.setIfMissing("spark.cassandra.connection.host", dbServer)
  conf.set("spark.cassandra.auth.username", dbUseraname)
  conf.set("spark.cassandra.auth.password", dbPassword)

  /**
    * Cassandra Properties
    */
  val keyspaceName = Try(config.getString("db.keyspaceName")).getOrElse("jactravel_monitoring_new")
}
