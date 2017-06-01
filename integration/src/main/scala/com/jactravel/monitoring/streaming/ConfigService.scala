package com.jactravel.monitoring.streaming

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.SparkConf

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

  private lazy val configRabbit : Config = ConfigFactory.load("reference.conf")

  val totalRegisters = 10000

  /**
    * Spark Properties
    */
  val conf = new SparkConf()
    .setAppName("logs-monitoring-receiver")
    .setIfMissing("spark.master", "local[*]")
  var ssc: StreamingContext = _

  /**
    * RabbitMQ Properties
    */
  val queueName = Try(configRabbit.getString("amqp.queueName")).getOrElse("rabbitmq-queue")
  val exchangeName = Try(configRabbit.getString("ammq.exchangeName")).getOrElse("rabbitmq-exchange")
  val exchangeType = Try(configRabbit.getString("amqp.exchangeType")).getOrElse("topic")
  val routingKey = Try(configRabbit.getString("rabbitmq.routingKey")).getOrElse("")
  val vHost = Try(configRabbit.getString("amqp.virtual-host")).getOrElse("/")
  val hosts = Try(configRabbit.getStringList("amqp.addresses.host").get(0)).getOrElse("ec2-34-225-142-10.compute-1.amazonaws.com")
  val username = Try(configRabbit.getString("amqp.username")).getOrElse("guest")
  val password = Try(configRabbit.getString("rabbitmq.password")).getOrElse("guest")

  /**
    * Cassandra Properties
    */
  val tableName = Try(configRabbit.getString("db.tableName")).getOrElse("clientsearch")
  val keyspaceName = Try(configRabbit.getString("db.keyspaceName")).getOrElse("jactravel_monitoring")
}
