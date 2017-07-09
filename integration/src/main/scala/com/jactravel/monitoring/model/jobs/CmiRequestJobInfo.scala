package com.jactravel.monitoring.model.jobs

import com.paulgoldbaum.influxdbclient.Point

/**
  * Created by fayaz on 09.07.17.
  */
trait CmiRequestJobInfo {
  case class CmiRequestCount(
                              cmi_count: Long,
                              login: String,
                              property_code: String,
                              cmi_query_type: Int
                            )

  case class CmiRequestSuccessCount(
                                     cmi_success_count: Long,
                                     login: String,
                                     property_code: String,
                                     cmi_query_type: Int
                                   )

  case class CmiRequestResponseTime(
                                     login: String,
                                     property_code: String,
                                     cmi_query_type: Int,
                                     min_response_time_ms: Long,
                                     max_response_time_ms: Long,
                                     perc_response_time_ms: Double
                                   )

  def toCmiCountPoint(crc: CmiRequestCount): Point = {
    Point("cmi_request_count")
      .addTag("login", crc.login)
      .addTag("property_code", crc.property_code)
      .addTag("cmi_query_type", crc.cmi_query_type.toString())
      .addField("cmi_count", crc.cmi_count)
  }

  def toCmiSuccessCountPoint(crsc: CmiRequestSuccessCount): Point = {
    Point("cmi_request_success_count")
      .addTag("login", crsc.login)
      .addTag("property_code", crsc.property_code)
      .addTag("cmi_query_type", crsc.cmi_query_type.toString())
      .addField("cmi_success_count", crsc.cmi_success_count)
  }


  def toResponseTimePoint(crrt: CmiRequestResponseTime): Point = {
    Point("cmi_request_response_time")
      .addTag("login", crrt.login)
      .addTag("property_code", crrt.property_code)
      .addTag("cmi_query_type", crrt.cmi_query_type.toString())
      .addField("min_response_time", crrt.min_response_time_ms)
      .addField("max_response_time", crrt.max_response_time_ms)
      .addField("perc_response_time", crrt.perc_response_time_ms)
  }
}
