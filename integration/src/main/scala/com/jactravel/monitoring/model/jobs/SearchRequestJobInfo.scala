package com.jactravel.monitoring.model.jobs

import com.paulgoldbaum.influxdbclient.Point

/**
  * Created by fayaz on 09.07.17.
  */
object SearchRequestJobInfo {
  case class SearchRequestCount(
                                 search_count: Long,
                                 time: String,
                                 brand_name: String,
                                 sales_channel: String,
                                 trade_group: String,
                                 trade_name: String,
                                 trade_parent_group: String,
                                 xml_booking_login: String
                               )

  case class SearchRequestSuccess(
                                   success_count: Long,
                                   time: String,
                                   brand_name: String,
                                   sales_channel: String,
                                   trade_group: String,
                                   trade_name: String,
                                   trade_parent_group: String,
                                   xml_booking_login: String
                                 )

  case class SearchRequestErrors(
                                  errors_count: Long,
                                  time: String,
                                  brand_name: String,
                                  sales_channel: String,
                                  trade_group: String,
                                  trade_name: String,
                                  trade_parent_group: String,
                                  xml_booking_login: String
                                )

  case class SearchRequestResponseTime(
                                        time: String,
                                        brand_name: String,
                                        sales_channel: String,
                                        trade_group: String,
                                        trade_name: String,
                                        trade_parent_group: String,
                                        xml_booking_login: String,
                                        min_response_time_ms: Long,
                                        max_response_time_ms: Long,
                                        perc_response_time_ms: Double
                                      )

}
