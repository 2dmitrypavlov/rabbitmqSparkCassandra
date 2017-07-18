package com.jactravel.monitoring.streaming.jobs

import com.jactravel.monitoring.model.jobs.CmiRequestJobInfo._
import com.pygmalios.reactiveinflux.spark._
import com.pygmalios.reactiveinflux.{ReactiveInfluxDbName, _}
import org.joda.time.DateTime

import scala.concurrent.duration._
import scala.util.Try

/**
  * Created by fayaz on 09.07.17.
  */
object CmiRequestJob extends JobConfig("cmi-request-job") {

  def main(args: Array[String]): Unit = {

    val nullFilter = Seq("login", "property_code", "cmi_query_type")

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

    // RICH CMI REQUEST
    spark.sql(
      """
        SELECT
           cm.query_uuid,
           cm.login,
           cm.property_code,
           cm.cmi_query_type,
           (unix_timestamp(client_request_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.sss') - unix_timestamp(client_response_utc_timestamp, 'yyyy-MM-dd HH:mm:ss.S')) as response_time_ms,
           success,
           window(client_request_utc_timestamp, '1 minute').end as time
        FROM PureCmiRequest as cm""")
      .createOrReplaceTempView("RichCmiRequest")

    // CMI COUNT
    val cmiCount = spark.sql(
      """
      SELECT COUNT(query_uuid) as cmi_count,
          login,
          property_code,
          cmi_query_type
      FROM RichCmiRequest
      GROUP BY
          time,
          login,
          property_code,
          cmi_query_type""")
      .na.fill("stub", nullFilter)
      .as[CmiRequestCount]

    // CMI SUCCESS COUNT
    val cmiSuccessCount = spark.sql(
      """
      SELECT COUNT(query_uuid) as cmi_success_count,
          login,
          property_code,
          cmi_query_type
      FROM RichCmiRequest
      WHERE success IS NOT NULL
      GROUP BY
          login,
          property_code,
          cmi_query_type""")
      .na.fill("stub", nullFilter)
      .as[CmiRequestSuccessCount]

    // CMI RESPONSE TIME
    val cmiResponseTime = spark.sql(
      """
      SELECT login,
          property_code,
          cmi_query_type,
          min(response_time_ms) as min_response_time_ms,
          max(response_time_ms) as max_response_time_ms,
          percentile_approx(response_time_ms, 0.5) as perc_response_time_ms
      FROM RichCmiRequest
      GROUP BY
          login,
          property_code,
          cmi_query_type""")
      .na.fill("stub", Seq("login", "property_code", "cmi_query_type"))
      .as[CmiRequestResponseTime]

    implicit val params = ReactiveInfluxDbName(influxDBname)
    implicit val awaitAtMost = 1.second
    cmiCount.rdd.map { src =>
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
    cmiSuccessCount.rdd.map { src =>
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

    cmiResponseTime.rdd.map { src =>
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
          , "perc_response_time" -> Try(src.perc_response_time_ms.toDouble).getOrElse(1)
        )
      )
    }.saveToInflux()


    //    cmiCount.foreachPartition { partition =>
    //
    //      // Open connection to Influxdb
    //      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)
    //
    //      partition
    //        .map(toCmiCountPoint)
    //        .foreach(p => Await.result(db.write(p), influxTimeout))
    //
    //      // Close connection
    //      db.close()
    //    }
    //
    //    cmiSuccessCount.foreachPartition { partition =>
    //
    //      // Open connection to Influxdb
    //      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)
    //
    //      partition
    //        .map(toCmiSuccessCountPoint)
    //        .foreach(p => Await.result(db.write(p), influxTimeout))
    //
    //      // Close connection
    //      db.close()
    //    }
    //
    //    cmiResponseTime.foreachPartition { partition =>
    //
    //      // Open connection to Influxdb
    //      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)
    //
    //      partition
    //        .map(toResponseTimePoint)
    //        .foreach(p => Await.result(db.write(p), influxTimeout))
    //
    //      // Close connection
    //      db.close()
    //
    //    }

    //    spark.stop()
  }
}
