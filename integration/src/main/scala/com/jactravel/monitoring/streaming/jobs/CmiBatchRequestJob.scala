package com.jactravel.monitoring.streaming.jobs

import com.jactravel.monitoring.model.jobs.CmiBatchRequestJobInfo
import com.jactravel.monitoring.streaming.ConfigService
import com.paulgoldbaum.influxdbclient.InfluxDB

import scala.concurrent.Await

/**
  * Created by fayaz on 09.07.17.
  */
object CmiBatchRequestJob extends ConfigService with CmiBatchRequestJobInfo with BaseJob {

  override val appName: String = "cmi_batch_request_job"

  def main(args: Array[String]): Unit = {

    val nullFilter = Seq("login","property_code")

    import spark.implicits._

    // CMI REQUEST
    spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map(
        "table" -> "cmi_request_updated_second",
        "keyspace" -> "jactravel_monitoring_new"))
      .load()
      .filter(query)
      .createOrReplaceTempView("PureCmiRequest")

    // CMI BATCH REQUEST
    spark
      .read
      .format("org.apache.spark.sql.cassandra")
      .options(Map(
        "table" -> "cmi_batch_request_updated_second",
        "keyspace" -> "jactravel_monitoring_new"))
      .load()
      .filter(query)
      .createOrReplaceTempView("PureCmiBatchRequest")

    // RICH CMI BATCH REQUEST
    spark.sql("""
      SELECT
         cmib.query_uuid,
         cmi.login  as login,
         cmi.property_code as property_code,
         cmib.cmi_query_type,
         (unix_timestamp(cmib.response_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.sss') - unix_timestamp(cmib.request_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.sss')) as processing_time_ms,
         (unix_timestamp(cmib.response_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.sss') - unix_timestamp(cmi.client_request_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.sss')) as total_processing_time_ms,
         cmib.success,
         window(cmib.request_utc_timestamp, '1 minute').end as time
      FROM PureCmiBatchRequest cmib
      LEFT JOIN PureCmiRequest cmi ON cmib.query_uuid = cmi.query_uuid""")
      .na.fill("unknown", nullFilter)
      .na.fill(-1, Seq("total_processing_time_ms"))
      .createOrReplaceTempView("RichCmiBatchRequest")

    // CMI BATCH COUNT
    val cmiBatchCount = spark.sql("""
      SELECT COUNT(query_uuid) as cmi_count,
          login,
          property_code,
          cmi_query_type
      FROM RichCmiBatchRequest
      GROUP BY
          time,
          login,
          property_code,
          cmi_query_type""")
      .na.fill("stub", Seq("cmi_query_type"))
      .as[CmiRequestCount]

    // CMI BATCH SUCCESS COUNT
    val cmiBatchSuccessCount = spark.sql("""
      SELECT COUNT(query_uuid) as cmi_success_count,
          login,
          property_code,
          cmi_query_type
      FROM RichCmiBatchRequest
      WHERE success IS NOT NULL
      GROUP BY
          login,
          property_code,
          cmi_query_type""")
      .na.fill("stub", Seq("cmi_success_count","login","property_code", "cmi_query_type"))
      .as[CmiRequestSuccessCount]

    // CMI BATCH RESPONSE TIME
    val cmiBatchResponseTime = spark.sql("""
      SELECT login,
          property_code,
          cmi_query_type,
          min(total_processing_time_ms) as min_response_time_ms,
          max(total_processing_time_ms) as max_response_time_ms,
          percentile_approx(processing_time_ms, 0.5) as perc_response_time_ms
      FROM RichCmiBatchRequest
      GROUP BY
          login,
          property_code,
          cmi_query_type""")
      .na.fill("stub", Seq("login","property_code", "cmi_query_type"))
      .as[CmiRequestResponseTime]

    // SAVING TO INFLUXDB

    // SAVING BOOK COUNT TO INFLUXDB
    cmiBatchCount.foreachPartition { partition =>

      // Open connection to Influxdb
      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)

      partition
        .map(toCmiCountPoint)
        .foreach(p => Await.result(db.write(p), timeout))

      // Close connection
      db.close()
    }

    // SAVING BOOK SUCCESS TO INFLUXDB
    cmiBatchSuccessCount.foreachPartition { partition =>

      // Open connection to Influxdb
      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)

      partition
        .map(toCmiSuccessCountPoint)
        .foreach(p => Await.result(db.write(p), timeout))

      // Close connection
      db.close()
    }

    // SAVING BOOK ERROR TO INFLUXDB
    cmiBatchResponseTime.foreachPartition { partition =>

      // Open connection to Influxdb
      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)

      partition
        .map(toResponseTimePoint)
        .foreach(p => Await.result(db.write(p), timeout))

      // Close connection
      db.close()
    }

    spark.stop()
  }
}
