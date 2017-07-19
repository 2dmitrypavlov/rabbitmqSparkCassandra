package com.jactravel.monitoring.model.jobs

import com.paulgoldbaum.influxdbclient.Point

/**
  * Created by fayaz on 09.07.17.
  */
object CmiBatchRequestJobInfo {

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

}
