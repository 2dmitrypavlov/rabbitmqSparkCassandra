package com.jactravel.monitoring.streaming.jobs

import com.jactravel.monitoring.model.jobs.CmiRequestJobInfo
import com.jactravel.monitoring.streaming.ConfigService
import com.paulgoldbaum.influxdbclient.InfluxDB

import scala.concurrent.Await

/**
  * Created by fayaz on 09.07.17.
  */
object CmiRequestJob extends ConfigService with CmiRequestJobInfo with BaseJob {

  override val appName: String = "cmi_request_job"

  def main(args: Array[String]): Unit = {

    val nullFilter = Seq("login","property_code", "cmi_query_type")

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
    spark.sql("""
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
    val cmiCount = spark.sql("""
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
    val cmiSuccessCount = spark.sql("""
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
    val cmiResponseTime = spark.sql("""
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
      .na.fill("stub", Seq("login","property_code", "cmi_query_type"))
      .as[CmiRequestResponseTime]

    cmiCount.foreachPartition { partition =>

      // Open connection to Influxdb
      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)

      partition
        .map(toCmiCountPoint)
        .foreach(p => Await.result(db.write(p), timeout))

      // Close connection
      db.close()
    }

    cmiSuccessCount.foreachPartition { partition =>

      // Open connection to Influxdb
      val db = InfluxDB.connect(influxHost, influxPort).selectDatabase(influxDBname)

      partition
        .map(toCmiSuccessCountPoint)
        .foreach(p => Await.result(db.write(p), timeout))

      // Close connection
      db.close()
    }

    cmiResponseTime.foreachPartition { partition =>

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
