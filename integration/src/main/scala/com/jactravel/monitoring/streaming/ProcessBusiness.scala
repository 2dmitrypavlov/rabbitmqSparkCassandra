package com.jactravel.monitoring.streaming

import java.sql.Date

import com.jactravel.monitoring.model._
import com.jactravel.monitoring.util.DateTimeUtils
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming._
import org.apache.spark.streaming.rabbitmq.RabbitMQUtils

/**
  * Created by dmitry on 7/4/17.
  */
case class Proxy(queryUUID: String, xmlBookingLogin: String)

object ProcessBusiness extends LazyLogging with ConfigService with ProcessMonitoringStream {
  def convertJavaDateToSqlDate(date: java.util.Date) = new Date(date.getTime)

  override val keyspaceName: String = "jactravel_monitoring_new"

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .config(conf)
      //.enableHiveSupport()
      .getOrCreate()
    import com.datastax.spark.connector.streaming._


    ///use get or create to use check point
    ssc = new StreamingContext(spark.sparkContext, Seconds(20))
    //Milliseconds(50))
    val numPar = 200

    val queryProxyStream1 = RabbitMQUtils.createStream[QueryProxyRequest](ssc
      , prepareQueueMap("QueryProxyRequest")
      , messageQueryProxyHandler).repartition(numPar)
    val queryProxyStream2 = RabbitMQUtils.createStream[QueryProxyRequest](ssc
      , prepareQueueMap("QueryProxyRequest")
      , messageQueryProxyHandler).repartition(numPar)
    val queryProxyStream3 = RabbitMQUtils.createStream[QueryProxyRequest](ssc
      , prepareQueueMap("QueryProxyRequest")
      , messageQueryProxyHandler).repartition(numPar)

    val queryProxyStream = queryProxyStream1.union(queryProxyStream2)
      .union(queryProxyStream3)
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


    val cmiRequestStream = RabbitMQUtils.createStream[CmiRequest](ssc
      , prepareQueueMap("CMIRequest")
      , messageCmiRequestHandler).repartition(numPar)

    val cmiBatchRequestStream = RabbitMQUtils.createStream[CmiBatchRequest](ssc
      , prepareQueueMap("CMIBatchRequest")
      , messageCmiBatchRequestHandler).repartition(numPar)

    ssc.remember(Seconds(120))
    //every
    //    val brand = spark
    //      .read
    //      .format("com.databricks.spark.csv")
    //      .option("header", "true") // Use first line of all files as header
    //      .option("inferSchema", "true") // Automatically infer data types
    //      .load(aws + "brand.csv")
    //    brand.createOrReplaceTempView("Brand")
    //    val trade = spark
    //      .read
    //      .format("com.databricks.spark.csv")
    //      .option("header", "true") // Use first line of all files as header
    //      .option("inferSchema", "true") // Automatically infer data types
    //      .load(aws + "trade.csv")
    //    trade.createOrReplaceTempView("Trade")
    //    val saleschannel = spark
    //      .read
    //      .format("com.databricks.spark.csv")
    //      .option("header", "true") // Use first line of all files as header
    //      .option("inferSchema", "true") // Automatically infer data types
    //      .load(aws + "saleschannel.csv")
    //    saleschannel.createOrReplaceTempView("SalesChannel")

    //    queryProxyStream.repartition(numPar).saveToCassandra(keyspaceName, "query_proxy_request_time")
    //    searchRequestStream.repartition(numPar).saveToCassandra(keyspaceName, "search_request_time")

    // BOOKING STREAM
    bookingStream.transform { rdd =>
      //      spark.createDataFrame(rdd).createOrReplaceTempView("BookRequest")
      //      rdd.take(1)
      rdd.map(l => BookRequest2(
        DateTimeUtils.parseDate(l.startUtcTimestamp).getTime / 1000L
        , l.queryUUID
        , l.searchQueryUUID
        , l.preBookQueryUUID
        , l.searchProcessor
        , l.host
        , l.startUtcTimestamp
        , l.endUtcTimestamp
        , l.tradeId
        , l.brandId
        , l.salesChannelId
        , l.propertyId
        , l.arrivalDate
        , l.duration
        , l.rooms
        , l.currencyId
        , l.success
        , l.errorMessage
        , l.errorStackTrace))
    }.saveToCassandra(keyspaceName, "book_request_second")

    // PRE-BOOKING STREAM
    preBookingStream.transform { rdd =>
      //      spark.createDataFrame(rdd).createOrReplaceTempView("pre_book_request")
      //      rdd.take(1)
      rdd.map(l => PreBookRequest2(
        DateTimeUtils.parseDate(l.startUtcTimestamp).getTime / 1000L
        , l.queryUUID
        , l.searchQueryUUID
        , l.searchProcessor
        , l.host
        , l.startUtcTimestamp
        , l.endUtcTimestamp
        , l.tradeId
        , l.brandId
        , l.salesChannelId
        , l.propertyId
        , l.arrivalDate
        , l.duration
        , l.rooms
        , l.currencyId
        , l.success
        , l.errorMessage
        , l.errorStackTrace))
    }.saveToCassandra(keyspaceName, "pre_book_request_second")

    // SEARCH REQUEST STREAM
    searchRequestStream.transform { rdd =>
      //      spark.createDataFrame(rdd).createOrReplaceTempView("search_request")
      //      rdd.take(1)
      rdd.map(l => SearchRequest2(
        DateTimeUtils.parseDate(l.requestInfo.startUtcTimestamp).getTime / 1000L
        , l.queryUUID
        , l.host
        , l.requestInfo
        , l.responseInfo
      ))
    }.saveToCassandra(keyspaceName, "search_request_second")

    // SUPPLIER BOOK REQUEST STREAM
    supplierBookhRequestStream.transform { rdd =>
      //      spark.createDataFrame(rdd).createOrReplaceTempView("supplier_book_request")
      //      rdd.take(1)
      rdd.map(l => SupplierBookRequest2(
        DateTimeUtils.parseDate(l.startUtcTimestamp).getTime / 1000L
        , l.queryUUID
        , l.host
        , l.source
        , l.startUtcTimestamp
        , l.endUtcTimestamp
        , l.timeout
        , l.propertyCount
        , l.success
        , l.errorMessage
        , l.errorStackTrace
        , l.requestXml
        , l.responseXml
        , l.requestCount
      ))
    }.saveToCassandra(keyspaceName, "supplier_book_request_second")

    // SUPPLIER PRE BOOK REQUEST STREAM
    supplierPreBookRequestStream.transform { rdd =>
      //      spark.createDataFrame(rdd).createOrReplaceTempView("supplier_pre_book_request")
      //      rdd.take(1)
      rdd.map(l => SupplierPreBookRequest2(
        DateTimeUtils.parseDate(l.startUtcTimestamp).getTime / 1000L
        , l.queryUUID
        , l.host
        , l.source
        , l.startUtcTimestamp
        , l.endUtcTimestamp
        , l.timeout
        , l.propertyCount
        , l.success
        , l.errorMessage
        , l.errorStackTrace
        , l.requestXml
        , l.responseXml
        , l.requestCount
      ))
    }.saveToCassandra(keyspaceName, "supplier_pre_book_request_second")

    // SUPPLIER SEARCH REQUEST STREAM
    supplierSearchRequestStream.transform { rdd =>
      //      spark.createDataFrame(rdd).createOrReplaceTempView("supplier_search_request")
      //      rdd.take(1)
      rdd.map(l => SupplierSearchRequest2(
        DateTimeUtils.parseDate(l.startUtcTimestamp).getTime / 1000L
        , l.queryUUID
        , l.host
        , l.source
        , l.startUtcTimestamp
        , l.endUtcTimestamp
        , l.timeout
        , l.propertyCount
        , l.success
        , l.errorMessage
        , l.errorStackTrace
        , l.requestXml
        , l.responseXml
        , l.requestCount
      ))
    }.saveToCassandra(keyspaceName, "supplier_search_request_second")

    // QUERY PROXY REQUEST STREAM
    queryProxyStream.transform { rdd =>
      //          spark.createDataFrame(rdd, QueryProxyRequest2.getClass).createOrReplaceTempView("QueryProxyRequest")
      //          rdd.take(1)
      rdd.map(l => QueryProxyRequest3(
        l.clientRequestUtcTimestamp.getTime / 1000L
        , l.queryUUID
        , l.clientIp
        , l.searchQueryType
        , l.host
        , convertJavaDateToSqlDate(l.clientRequestUtcTimestamp)
        , convertJavaDateToSqlDate(l.clientResponseUtcTimestamp)
        , convertJavaDateToSqlDate(l.forwardedRequestUtcTimestamp)
        , convertJavaDateToSqlDate(l.forwardedResponseUtcTimestamp)
        , l.requestXml
        , l.responseXml
        , l.xmlBookingLogin
        , l.success
        , l.errorMessage
        , l.requestProcessor
        , l.requestURL
        , l.errorStackTrace
      ))
    }.saveToCassandra(keyspaceName, "query_proxy_request_second")


    // CMI REQUEST STREAMING
    cmiRequestStream.transform { rdd =>
      //      spark.createDataFrame(rdd).createOrReplaceTempView("cmi_request")
      //      rdd.take(1)
      rdd.map(l => CmiRequest2(
        DateTimeUtils.parseDate(l.clientRequestUtcTimestamp).getTime / 1000L
        , l.queryUUID
        , l.supplierIp
        , l.cmiQueryType
        , l.host
        , l.clientRequestUtcTimestamp
        , l.clientResponseUtcTimestamp
        , l.forwardedRequestUtcTimestamp
        , l.forwardedResponseUtcTimestamp
        , l.requestXml
        , l.responseXml
        , l.login
        , l.propertyCode
        , l.success
        , l.errorMessage
        , l.requestProcessor
        , l.requestURL
        , l.errorStackTrace
      ))
    }.saveToCassandra(keyspaceName, "cmi_request_second")


    cmiBatchRequestStream.transform { rdd =>
      rdd.map { l =>
        CmiBatchRequest2(
          DateTimeUtils.parseDate(l.requestUtcTimestamp).getTime / 1000L
          , l.queryUUID
          , l.supplierIp
          , l.cmiQueryType
          , l.host
          , l.requestUtcTimestamp
          , l.responseUtcTimestamp
          , l.requestXml
          , l.responseXml
          , l.login
          , l.propertyCode
          , l.success
          , l.errorMessage
          , l.errorStackTrace)
      }

    }.repartition(numPar).saveToCassandra(keyspaceName, "cmi_batch_request")


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
      //, "maxMessagesPerPartition" -> "100"
      //, "levelParallelism" -> "100"
      , "rememberDuration" -> "3000000"
      //, "maxReceiveTime" -> "500"
      , "storageLevel" -> "MEMORY_AND_DISK"

    )
  }
}

//streaming joins
//    queryProxyStream.transform { rdd =>
//      spark.createDataFrame(rdd.map(l => Proxy(l.queryUUID, l.xmlBookingLogin)))
//        .createOrReplaceTempView("QueryProxyRequest")
//
//      //here we put all the sqls
//
//      // BOOKING
//      spark.sql(
//        """
//        SELECT br.queryUUID as query_uuid,
//               brand_name,
//               trade_name,
//               trade_group,
//               trade_parent_group,
//               sales_channel,
//               (unix_timestamp(endUtcTimestamp) - unix_timestamp(startUtcTimestamp)) * 1000 as response_time_ms,
//               br.errorStackTrace,
//               br.success,
//               window(startUtcTimestamp, '5 minutes').end as time,
//               xmlBookingLogin
//        FROM BookRequest as br
//        LEFT JOIN Brand as b
//        ON br.brandId == b.brand_id
//        LEFT JOIN Trade as t
//        ON br.tradeId == t.trade_id
//        LEFT JOIN QueryProxyRequest as qpr
//        ON br.queryUUID == qpr.queryUUID
//        LEFT JOIN SalesChannel as sc
//        ON br.salesChannelId == sc.sales_channel_id
//        """).createOrReplaceTempView("BookingEnriched")
//
//      // BOOKING COUNT
//      spark.sql(
//        """
//      SELECT COUNT(query_uuid) as booking_count,
//          time,
//          brand_name,
//          sales_channel,
//          trade_group,
//          trade_name,
//          trade_parent_group,
//          xmlBookingLogin
//      from BookingEnriched
//      group by
//          time,
//          brand_name,
//          sales_channel,
//          trade_group,
//          trade_name,
//          trade_parent_group,
//          xmlBookingLogin
//
//      """).createOrReplaceTempView("BookingCount")
//      //val bookCount = Encoders.bean(classOf[BookRequestCount])
//      val data = spark.sql(
//        """select booking_count,
//                                      time as tm,
//                                      brand_name,
//                                      sales_channel,
//                                      trade_group,
//                                      trade_name,
//                                      trade_parent_group,
//                                      xmlBookingLogin
//                                       from BookingCount""")
//      spark.sql("select * from QueryProxyRequest").write.mode(SaveMode.Append).format("parquet").save(aws+"proxy")
//      spark.sql("select * from BookingEnriched").write.mode(SaveMode.Append).format("parquet").save(aws+"book")
//      data.write.mode(SaveMode.Append).format("parquet").save(aws+"data")
//
//      // BOOKING SUCCESS
//      //      val bookingSucces = spark.sql("""
//      //      SELECT COUNT(query_uuid) as booking_success,
//      //          time,
//      //          brand_name,
//      //          sales_channel,
//      //          trade_group,
//      //          trade_name,
//      //          trade_parent_group,
//      //          xmL_booking_login
//      //      FROM BookingEnriched
//      //      WHERE success IS NOT NULL
//      //      GROUP BY
//      //          time,
//      //          brand_name,
//      //          sales_channel,
//      //          trade_group,
//      //          trade_name,
//      //          trade_parent_group,
//      //          xml_booking_login
//      //      """).createOrReplaceTempView("BookingSuccess")
//      //
//      //      // BOOKING ERROR
//      //      spark.sql("""
//      //      SELECT COUNT(query_uuid) as booking_errors,
//      //          time,
//      //          brand_name,
//      //          sales_channel,
//      //          trade_group,
//      //          trade_name,
//      //          trade_parent_group,
//      //          xmL_booking_login
//      //      FROM BookingEnriched
//      //      WHERE error_stack_trace IS NOT NULL
//      //      GROUP BY
//      //          time,
//      //          brand_name,
//      //          sales_channel,
//      //          trade_group,
//      //          trade_name,
//      //          trade_parent_group,
//      //          xml_booking_login
//      //      """).createOrReplaceTempView("BookingError")
//      //      // BOOKING RESPONSE TIME
//      //      spark.sql("""
//      //      SELECT COUNT(query_uuid) as booking_errors,
//      //          time,
//      //          brand_name,
//      //          sales_channel,
//      //          trade_group,
//      //          trade_name,
//      //          trade_parent_group,
//      //          xmL_booking_login,
//      //          min(response_time_ms) as min_response_time_ms,
//      //          max(response_time_ms) as max_response_time_ms,
//      //          avg(response_time_ms) as avg_response_time_ms
//      //      FROM BookingEnriched
//      //      GROUP BY
//      //          time,
//      //          brand_name,
//      //          sales_channel,
//      //          trade_group,
//      //          trade_name,
//      //          trade_parent_group,
//      //          xml_booking_login
//      //      """).createOrReplaceTempView("BookingResponse")
//      //      rdd.take(1)
//
//      data.rdd
//        .map { case r: Row => BookRequestCount(r.getAs("booking_count"), r.getAs("tm")
//          , r.getAs("brand_name"), r.getAs("sales_channel"), r.getAs("trade_group"), r.getAs("trade_name")
//          , r.getAs("trade_parent_group"), r.getAs("xmlBookingLogin"))
//        }
//
//    }.saveAsTextFiles(aws+"")

//    cmiBatchRequestStream.transform{rdd=>
//      import org.apache.spark.sql.Encoders
//      val bookCount = Encoders.bean(classOf[BookRequestCount])
//      val data= spark.sql("""select booking_count,
//                                time,
//                                brand_name,
//                                sales_channel,
//                                trade_group,
//                                trade_name,
//                                trade_parent_group,
//                                xml_booking_login
//                                 from BookingCount""").rdd.map { case r:Row => r.getAs[BookRequestCount]("_2")}
//
//      //.as[BookRequestCount](bookCount).rdd
//
//      // .rdd.map { case r:Row => r.getAs[BookRequestCount]("_2")}
//      data
//    }.saveToCassandra(keyspaceName, "booking_count")


// Start the computation