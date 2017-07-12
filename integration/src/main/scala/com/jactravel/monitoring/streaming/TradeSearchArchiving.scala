package com.jactravel.monitoring.streaming

import com.jactravel.monitoring.PlatformType
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.SparkConf
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Row, SaveMode, SparkSession}
import org.apache.spark.streaming.dstream.ConstantInputDStream
import org.apache.spark.streaming.{Minutes, StreamingContext}
import org.joda.time.{DateTime, DateTimeZone}

import scala.language.postfixOps
import scala.util.Try

/**
  * Created by eugene on 5/30/17.
  */

object TradeSearchArchiving extends LazyLogging with ConfigService with ProcessMonitoringStream {

  def main(args: Array[String]): Unit = {

    import com.datastax.spark.connector.streaming._

    val conf = new SparkConf()
      .setAppName("search_request_archiving")
      .setIfMissing("spark.master", "local[*]")

    conf.setIfMissing("spark.cassandra.connection.host", dbServer)
    conf.set("spark.cassandra.auth.username", dbUseraname)
    conf.set("spark.cassandra.auth.password", dbPassword)
    conf.set("spark.cassandra.connection.keep_alive_ms", "60000")

    ssc = new StreamingContext(conf, Minutes(5))
    ssc.sparkContext.setLogLevel("ERROR")

    val searchStream = ssc
      .cassandraTable(keyspaceName, "search_request_second").toEmptyCassandraRDD

    val dstream = new ConstantInputDStream(ssc, searchStream)

    dstream.foreachRDD {
      rdd =>
        val spark = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()

        import spark.implicits._

        val lowerSearch = DateTime.now().withZone(DateTimeZone.UTC).minusMinutes(5).getMillis / 1000
        val lowerProxy = DateTime.now().withZone(DateTimeZone.UTC).minusMinutes(5).minusSeconds(5).getMillis / 1000
        val upper = DateTime.now().withZone(DateTimeZone.UTC).getMillis / 1000
        val rangeSearch = lowerSearch to upper by 1
        val rangeProxy = lowerProxy to upper by 1

        val dfSearchRequest = spark
          .read
          .format(CassandraFormat)
          .options(Map("table" -> "search_request_second", "keyspace" -> keyspaceName))
          .load
          .select($"query_second"
            , $"query_uuid"
            , $"request_info"
            , $"response_info.property_reference_count"
            , $"response_info.property_count"
            , $"response_info.priced_room_count"
            , $"response_info.success"
          )

        val dfQueryProxRequest = spark
          .read
          .format("org.apache.spark.sql.cassandra")
          .options(Map("table" -> "query_proxy_request_second", "keyspace" -> keyspaceName))
          .load
          .select($"query_second"
            , $"query_uuid"
            , $"xml_booking_login"
            , $"request_processor"
            , $"client_request_utc_timestamp"
            , $"client_response_utc_timestamp")

        val dfSearchRequestFiltered = dfSearchRequest
          .where($"query_second".isin(rangeSearch.toList: _*))

        val dfQueryProxyRequestRenamed = dfQueryProxRequest
          .withColumnRenamed("query_second", "query_proxy_second")
          .where($"query_proxy_second".isin(rangeProxy.toList: _*))
        val dfRenamed = dfSearchRequestFiltered
          .join(dfQueryProxyRequestRenamed
            , dfSearchRequestFiltered("query_uuid") === dfQueryProxyRequestRenamed("query_uuid")
            , "left_outer")

        val toProcessor = udf[String, Int] { processorId =>
          Try(PlatformType.valueOf(processorId).getValueDescriptor.getName) getOrElse PlatformType.UnknownPlatform.toString
        }

        val adultsCount = udf {
          (rooms: Seq[Row]) => rooms.foldLeft(0)(_ + _.getAs[Int]("adults"))
        }

        val childrenCount = udf {
          (rooms: Seq[Row]) => rooms.foldLeft(0)(_ + _.getAs[Int]("children"))
        }

        val df = dfRenamed
          .withColumn("search_date",
            coalesce($"client_request_utc_timestamp", $"request_info.start_utc_timestamp")
              .cast("timestamp"))
          .withColumn("client_response_utc_timestamp",
            coalesce($"client_response_utc_timestamp", $"request_info.end_utc_timestamp")
              .cast("timestamp"))
          .withColumn("diff_request_time",
            unix_timestamp($"client_response_utc_timestamp") - unix_timestamp($"search_date"))
          .withColumn("diff_response_time",
            unix_timestamp($"request_info.end_utc_timestamp") - unix_timestamp($"request_info.start_utc_timestamp"))
          .withColumn("search_hour", hour($"search_date"))
          .withColumn("request_processor_name", toProcessor($"request_processor"))
          .withColumn("adults", adultsCount($"request_info.rooms"))
          .withColumn("children", childrenCount($"request_info.rooms"))
          .selectExpr(
            "search_date"
            , "diff_request_time"
            , "search_hour"
            , "request_info.trade_id as trade_id"
            , "request_info.brand_id as brand_id"
            , "request_info.sales_channel_id as sales_channel_id"
            , "request_info.search_geo_level as search_geo_level"
            , "request_info.geo_level1_id as geo_level1_id"
            , "request_info.geo_level2_id as geo_level2_id"
            , "request_info.arrival_date as arrival_date"
            , "request_info.duration as duration"
            , "adults"
            , "children"
            , "success as success"
            , "request_info.room_count as room_count"
            , "request_processor_name"
            , "xml_booking_login"
            , "request_processor_name"
            , "property_reference_count"
            , "property_count"
            , "priced_room_count"
            , "diff_response_time"
          )


        df.createOrReplaceTempView("search_query_proxy")

        val tradeSearchArchiveDF = spark.sqlContext
          .sql(
            """SELECT search_date,
               trade_id,
               coalesce(xml_booking_login, 'unknown') as xml_book_login,
               search_hour,
               request_processor_name as request_processor,
               brand_id,
               sales_channel_id,
               search_geo_level,
               geo_level1_id,
               geo_level2_id,
               arrival_date,
               duration,
               adults,
               children,
               success,
               SUM(property_reference_count) as property_reference_count,
               SUM(property_count) as property_count,
               SUM(diff_request_time) as total_time,
               percentile(diff_request_time, 0.5) as total_time_p50,
               percentile(diff_request_time, 0.95) as total_time_p95,
               percentile(diff_request_time, 0.99) as total_time_p99,
               SUM(diff_response_time) processing_time,
               percentile(diff_response_time, 0.5) as processing_time_p50,
               percentile(diff_response_time, 0.95) as processing_time_p95,
               percentile(diff_response_time, 0.99) as processing_time_p99,
               COUNT(*) as search_count
               FROM search_query_proxy
               GROUP BY
               search_date,
               trade_id,
               xml_booking_login,
               search_hour,
               request_processor_name,
               brand_id,
               sales_channel_id,
               search_geo_level,
               geo_level1_id,
               geo_level2_id,
               arrival_date,
               duration,
               adults,
               children,
               success
              """)

        tradeSearchArchiveDF.show()
        // Save to Cassandra
        tradeSearchArchiveDF
          .write
          .format(CassandraFormat)
          .mode(SaveMode.Append)
          .options(Map("table" -> "trade_search_archive", "keyspace" -> keyspaceName))
          .save()
    }

    // Start the computation
    ssc.start()

    // Termination
    ssc.awaitTermination()
  }
}