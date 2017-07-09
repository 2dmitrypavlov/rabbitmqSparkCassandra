package com.jactravel.monitoring.model.jobs

/**
  * Created by dmitry on 7/9/17.
  */
object CaseClassed {

  case class BookRequestCount(
                               book_count: Long,
                               time: String,
                               brand_name: String,
                               sales_channel: String,
                               trade_group: String,
                               trade_name: String,
                               trade_parent_group: String,
                               xml_booking_login: String
                             )

  case class BookRequestSuccessCount(
                                      success_count: Long,
                                      time: String,
                                      brand_name: String,
                                      sales_channel: String,
                                      trade_group: String,
                                      trade_name: String,
                                      trade_parent_group: String,
                                      xml_booking_login: String
                                    )

  case class BookRequestErrorsCount(
                                     errors_count: Long,
                                     time: String,
                                     brand_name: String,
                                     sales_channel: String,
                                     trade_group: String,
                                     trade_name: String,
                                     trade_parent_group: String,
                                     xml_booking_login: String
                                   )

  case class BookRequestResponseTime(
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