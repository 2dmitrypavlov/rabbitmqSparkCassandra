package com.jactravel.monitoring.model.jobs

import com.paulgoldbaum.influxdbclient.Point

/**
  * Created by fayaz on 09.07.17.
  */
object SupplierSearchRequestJobInfo {
  case class SupplierSearchRequestInfo(
                                        time: String,
                                        source: String,
                                        brand_name: String,
                                        trade_group: String,
                                        trade_name: String,
                                        trade_parent_group: String,
                                        xml_booking_login: String,
                                        client_search_number: Long,
                                        min_response_time: Long,
                                        max_response_time: Long,
                                        avg_response_time: Double,
                                        search_timeout_number: Long,
                                        min_property_number: Long,
                                        avg_property_number: Double,
                                        max_property_number: Long,
                                        success_number: Long,
                                        error_number: Long,
                                        search_number: Long
                                      )
}
