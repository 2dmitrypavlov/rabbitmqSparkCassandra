package com.jactravel.monitoring.jobs

import com.jactravel.monitoring.model.jobs.CmiBatchRequestJobInfo._
import com.paulgoldbaum.influxdbclient.Point
import com.pygmalios.reactiveinflux.{ReactiveInfluxDbName, _}
import com.pygmalios.reactiveinflux.spark._
import org.joda.time.DateTime

import scala.concurrent.duration._
import scala.util.Try

/**
  * Created by fayaz on 09.07.17.
  */
object CmiBatchRequestJob extends JobConfig("cmi-batch-request-job") {

  def main(args: Array[String]): Unit = {

    import spark.implicits._

    val nullFilter = Seq("login", "property_code")

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
    spark.sql(
      """
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
    val cmiBatchCount = spark.sql(
      """
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
    val cmiBatchSuccessCount = spark.sql(
      """
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
      .na.fill("stub", Seq("cmi_success_count", "login", "property_code", "cmi_query_type"))
      .as[CmiRequestSuccessCount]

    // CMI BATCH RESPONSE TIME
    val cmiBatchResponseTime = spark.sql(
      """
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
      .na.fill("stub", Seq("login", "property_code", "cmi_query_type"))
      .as[CmiRequestResponseTime]

    // SAVING TO INFLUXDB
    def toCmiCountPoint(crc: CmiRequestCount): Point = {
      Point("cmi_bacth_request_count")
        .addTag("login", crc.login)
        .addTag("property_code", crc.property_code)
        .addTag("cmi_query_type", crc.cmi_query_type.toString())
        .addField("cmi_count", crc.cmi_count)
    }


    cmiBatchCount.rdd.map { src =>
      com.pygmalios.reactiveinflux.Point(
        time = DateTime.now(),
        measurement = "cmi_bacth_request_count",
        tags = Map(
          "login" -> Try(src.login).getOrElse("no_login")
          , "property_code" -> Try(src.property_code).getOrElse("no_property_code")
          , "cmi_query_type" -> Try(src.cmi_query_type.toString).getOrElse("no_cmi_query_type")
        ),
        fields = Map(
          "cmi_count" -> Try(src.cmi_count.toInt).getOrElse(1)
        )
      )
    }.saveToInflux()


    cmiBatchSuccessCount.rdd.map { src =>
      com.pygmalios.reactiveinflux.Point(
        time = DateTime.now(),
        measurement = "cmi_batch_request_success_count",
        tags = Map(
          "login" -> Try(src.login).getOrElse("no_login")
          , "property_code" -> Try(src.property_code).getOrElse("no_property_code")
          , "cmi_query_type" -> Try(src.cmi_query_type.toString).getOrElse("no_cmi_query_type")
        ),
        fields = Map(
          "cmi_success_count" -> Try(src.cmi_success_count.toInt).getOrElse(1)
        )
      )
    }.saveToInflux()

    cmiBatchResponseTime.rdd.map { src =>
      com.pygmalios.reactiveinflux.Point(
        time = DateTime.now(),
        measurement = "cmi_batch_request_response_time",
        tags = Map(
          "login" -> Try(src.login).getOrElse("no_login")
          , "property_code" -> Try(src.property_code).getOrElse("no_property_code")
          , "cmi_query_type" -> Try(src.cmi_query_type.toString).getOrElse("no_cmi_query_type")
        ),
        fields = Map(
          "min_response_time" -> Try(src.min_response_time_ms.toInt).getOrElse(1)
          , "max_response_time" -> Try(src.max_response_time_ms.toInt).getOrElse(1)
          , "perc_response_time" -> Try(src.perc_response_time_ms.toDouble).getOrElse(1.0)
        )
      )
    }.saveToInflux()

  }
}
