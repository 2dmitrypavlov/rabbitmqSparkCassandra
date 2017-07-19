package com.jactravel.monitoring.model.jobs

/**
  * Created by fayaz on 09.07.17.
  */
object PreBookRequestJobInfo {
  case class PreBookRequestCount(
                                  pre_book_count: Long,
                                  time: String,
                                  brand_name: String,
                                  sales_channel: String,
                                  trade_group: String,
                                  trade_name: String,
                                  trade_parent_group: String,
                                  xml_booking_login: String
                                )

  case class PreBookRequestSuccessCount(
                                         success_count: Long,
                                         time: String,
                                         brand_name: String,
                                         sales_channel: String,
                                         trade_group: String,
                                         trade_name: String,
                                         trade_parent_group: String,
                                         xml_booking_login: String
                                       )

  case class PreBookRequestErrorsCount(
                                        errors_count: Long,
                                        time: String,
                                        brand_name: String,
                                        sales_channel: String,
                                        trade_group: String,
                                        trade_name: String,
                                        trade_parent_group: String,
                                        xml_booking_login: String
                                      )

  case class PreBookRequestResponseTime(
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
