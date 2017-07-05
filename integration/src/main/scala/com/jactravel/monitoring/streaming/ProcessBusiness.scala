package com.jactravel.monitoring.streaming

import com.jactravel.monitoring.model._
import com.jactravel.monitoring.model.influx.BookRequestInflux.BookRequestCount
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.streaming.rabbitmq.RabbitMQUtils
import org.apache.spark.streaming._

/**
  * Created by dmitry on 7/4/17.
  */
case class Proxy(queryUUID: String, xmlBookingLogin: String)

object ProcessBusiness extends LazyLogging with ConfigService with ProcessMonitoringStream {

  override val keyspaceName: String = "jactravel_monitoring_new"

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .config(conf)
      //.enableHiveSupport()
      .getOrCreate()
    import com.datastax.spark.connector.streaming._


    ///use get or create to use check point
    ssc = new StreamingContext(spark.sparkContext, Seconds(1))
    //Milliseconds(50))
    val numPar = 150

    val bookingStream = RabbitMQUtils.createStream[BookRequest](ssc
      , prepareQueueMap("BookRequest")
      , messageBookingHandler).repartition(numPar)

    val preBookingStream = RabbitMQUtils.createStream[PreBookRequest](ssc
      , prepareQueueMap("PreBookRequest")
      , messagePreBookingHandler).repartition(numPar)

    val searchRequestStream = RabbitMQUtils.createStream[SearchRequest](ssc
      , prepareQueueMap("SearchRequest")
      , messageSearchRequestHandler).repartition(numPar)

    val supplierBookhRequestStream = RabbitMQUtils.createStream[SupplierBookRequest](ssc
      , prepareQueueMap("SupplierBookRequest")
      , messageSupplierBookRequestHandler).repartition(numPar)

    val supplierPreBookRequestStream = RabbitMQUtils.createStream[SupplierPreBookRequest](ssc
      , prepareQueueMap("SupplierPreBookRequest")
      , messageSupplierPreBookRequestHandler).repartition(numPar)

    val supplierSearchRequestStream = RabbitMQUtils.createStream[SupplierSearchRequest](ssc
      , prepareQueueMap("SupplierSearchRequest")
      , messageSupplierSearchRequestHandler).repartition(numPar)
    //
    val queryProxyStream = RabbitMQUtils.createStream[QueryProxyRequest](ssc
      , prepareQueueMap("QueryProxyRequest")
      , messageQueryProxyHandler).repartition(numPar)

    val cmiRequestStream = RabbitMQUtils.createStream[CmiRequest](ssc
      , prepareQueueMap("CMIRequest")
      , messageCmiRequestHandler).repartition(numPar)

    val cmiBatchRequestStream = RabbitMQUtils.createStream[CmiBatchRequest](ssc
      , prepareQueueMap("CMIBatchRequest")
      , messageCmiBatchRequestHandler).repartition(numPar)

    ssc.remember(Duration(1800000L))
    //every
    val brand = spark
      .read
      .format("com.databricks.spark.csv")
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load(aws + "brand.csv")
    brand.createOrReplaceTempView("Brand")
    val trade = spark
      .read
      .format("com.databricks.spark.csv")
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load(aws + "trade.csv")
    trade.createOrReplaceTempView("Trade")
    val saleschannel = spark
      .read
      .format("com.databricks.spark.csv")
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load(aws + "saleschannel.csv")
    saleschannel.createOrReplaceTempView("SalesChannel")


    bookingStream.transform { rdd =>
      spark.createDataFrame(rdd).createOrReplaceTempView("BookRequest")
      rdd.take(1)
      rdd
    }.saveToCassandra(keyspaceName, "book_request")
    preBookingStream.transform { rdd =>
      spark.createDataFrame(rdd).createOrReplaceTempView("pre_book_request")
      rdd.take(1)
      rdd
    }.saveToCassandra(keyspaceName, "pre_book_request")
    searchRequestStream.transform { rdd =>
      spark.createDataFrame(rdd).createOrReplaceTempView("search_request")
      rdd.take(1)
      rdd
    }.saveToCassandra(keyspaceName, "search_request")
    supplierBookhRequestStream.transform { rdd =>
      spark.createDataFrame(rdd).createOrReplaceTempView("supplier_book_request")
      rdd.take(1)
      rdd
    }.saveToCassandra(keyspaceName, "supplier_book_request")
    supplierPreBookRequestStream.transform { rdd =>
      spark.createDataFrame(rdd).createOrReplaceTempView("supplier_pre_book_request")
      rdd.take(1)
      rdd
    }.saveToCassandra(keyspaceName, "supplier_pre_book_request")
    supplierSearchRequestStream.transform { rdd =>
      spark.createDataFrame(rdd).createOrReplaceTempView("supplier_search_request")
      rdd.take(1)
      rdd
    }.saveToCassandra(keyspaceName, "supplier_search_request")
    //    queryProxyStream.transform{rdd=>spark.createDataFrame(rdd, QueryProxyRequest2.getClass).createOrReplaceTempView("QueryProxyRequest")
    //      rdd.take(1)
    //      rdd }.saveToCassandra(keyspaceName, "query_proxy_request")
    cmiRequestStream.transform { rdd =>
      spark.createDataFrame(rdd).createOrReplaceTempView("cmi_request")
      rdd.take(1)
      rdd
    }.saveToCassandra(keyspaceName, "cmi_request")

    queryProxyStream.transform { rdd =>
      spark.createDataFrame(rdd.map(l => Proxy(l.queryUUID, l.xmlBookingLogin)))
        .createOrReplaceTempView("QueryProxyRequest")

      //here we put all the sqls

      // BOOKING
      spark.sql(
        """
        SELECT br.queryUUID as query_uuid,
               brand_name,
               trade_name,
               trade_group,
               trade_parent_group,
               sales_channel,
               (unix_timestamp(endUtcTimestamp) - unix_timestamp(startUtcTimestamp)) * 1000 as response_time_ms,
               br.errorStackTrace,
               br.success,
               window(startUtcTimestamp, '5 minutes').end as time,
               xmlBookingLogin
        FROM BookRequest as br,
             SalesChannel as sc,
             Trade as t,
             Brand as b
        LEFT JOIN QueryProxyRequest as qpr
        ON br.searchQueryUUID == qpr.queryUUID
        WHERE br.salesChannelId == sc.sales_channel_id
        AND br.tradeId == t.trade_id
        AND br.brandId == b.brand_id""").createOrReplaceTempView("BookingEnriched")

      // BOOKING COUNT
      spark.sql(
        """
      SELECT COUNT(query_uuid) as booking_count,
          time,
          brand_name,
          sales_channel,
          trade_group,
          trade_name,
          trade_parent_group,
          xmlBookingLogin
      from BookingEnriched
      group by
          time,
          brand_name,
          sales_channel,
          trade_group,
          trade_name,
          trade_parent_group,
          xmlBookingLogin

      """).createOrReplaceTempView("BookingCount")
      //val bookCount = Encoders.bean(classOf[BookRequestCount])
      val data = spark.sql(
        """select booking_count,
                                      time as tm,
                                      brand_name,
                                      sales_channel,
                                      trade_group,
                                      trade_name,
                                      trade_parent_group,
                                      xmlBookingLogin
                                       from BookingCount""").rdd
        .map { case r: Row => BookRequestCount(r.getAs("booking_count"), r.getAs("tm")
          , r.getAs("brand_name"), r.getAs("sales_channel"), r.getAs("trade_group"), r.getAs("trade_name")
          , r.getAs("trade_parent_group"), r.getAs("xmlBookingLogin"))
        }
      data.saveAsTextFile(aws+"temp")

      // BOOKING SUCCESS
      //      val bookingSucces = spark.sql("""
      //      SELECT COUNT(query_uuid) as booking_success,
      //          time,
      //          brand_name,
      //          sales_channel,
      //          trade_group,
      //          trade_name,
      //          trade_parent_group,
      //          xmL_booking_login
      //      FROM BookingEnriched
      //      WHERE success IS NOT NULL
      //      GROUP BY
      //          time,
      //          brand_name,
      //          sales_channel,
      //          trade_group,
      //          trade_name,
      //          trade_parent_group,
      //          xml_booking_login
      //      """).createOrReplaceTempView("BookingSuccess")
      //
      //      // BOOKING ERROR
      //      spark.sql("""
      //      SELECT COUNT(query_uuid) as booking_errors,
      //          time,
      //          brand_name,
      //          sales_channel,
      //          trade_group,
      //          trade_name,
      //          trade_parent_group,
      //          xmL_booking_login
      //      FROM BookingEnriched
      //      WHERE error_stack_trace IS NOT NULL
      //      GROUP BY
      //          time,
      //          brand_name,
      //          sales_channel,
      //          trade_group,
      //          trade_name,
      //          trade_parent_group,
      //          xml_booking_login
      //      """).createOrReplaceTempView("BookingError")
      //      // BOOKING RESPONSE TIME
      //      spark.sql("""
      //      SELECT COUNT(query_uuid) as booking_errors,
      //          time,
      //          brand_name,
      //          sales_channel,
      //          trade_group,
      //          trade_name,
      //          trade_parent_group,
      //          xmL_booking_login,
      //          min(response_time_ms) as min_response_time_ms,
      //          max(response_time_ms) as max_response_time_ms,
      //          avg(response_time_ms) as avg_response_time_ms
      //      FROM BookingEnriched
      //      GROUP BY
      //          time,
      //          brand_name,
      //          sales_channel,
      //          trade_group,
      //          trade_name,
      //          trade_parent_group,
      //          xml_booking_login
      //      """).createOrReplaceTempView("BookingResponse")
      //      rdd.take(1)
      data
    }.saveToCassandra(keyspaceName, "book_request_count")


    //    cmiBatchRequestStream.transform{rdd=>
    //      import org.apache.spark.sql.Encoders
    //      val bookCount = Encoders.bean(classOf[BookRequestCount])
    //     val data= spark.sql("""select booking_count,
    //                            time,
    //                            brand_name,
    //                            sales_channel,
    //                            trade_group,
    //                            trade_name,
    //                            trade_parent_group,
    //                            xml_booking_login
    //                             from BookingCount""").rdd.map { case r:Row => r.getAs[BookRequestCount]("_2")}
    //
    //       //.as[BookRequestCount](bookCount).rdd
    //
    //      // .rdd.map { case r:Row => r.getAs[BookRequestCount]("_2")}
    //    data
    //      }.saveToCassandra(keyspaceName, "booking_count")

    // Start the computation
    ssc.start()

    // Termination
    ssc.awaitTermination()

  }

  private[this] def prepareQueueMap(queueName: String) = {
    Map(
      "hosts" -> hosts
      , "queueName" -> queueName
      //      , "exchangeName" -> exchangeName
      //      , "exchangeType" -> exchangeType
      , "vHost" -> vHost
      , "userName" -> username
      , "password" -> password
      , "routingKey" -> queueName
      // ,"maxMessagesPerPartition"->"1"
      , "maxMessagesPerPartition" -> "100"
      , "levelParallelism" -> "100"
      , "rememberDuration" -> "1800000"
      , "maxReceiveTime" -> "500"
      , "storageLevel" -> "MEMORY_AND_DISK"

    )
  }
}
