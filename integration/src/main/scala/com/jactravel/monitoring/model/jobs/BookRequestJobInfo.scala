package com.jactravel.monitoring.model.jobs

import com.paulgoldbaum.influxdbclient.Point

/**
  * Created by fayaz on 09.07.17.
  */
object BookRequestJobInfo {

  case class BookRequestCount(book_count: Long,
                              time: String,
                              brand_name: String,
                              sales_channel: String,
                              trade_group: String,
                              trade_name: String,
                              trade_parent_group: String,
                              xml_booking_login: String)

  case class BookRequestSuccessCount(success_count: Long,
                                     time: String,
                                     brand_name: String,
                                     sales_channel: String,
                                     trade_group: String,
                                     trade_name: String,
                                     trade_parent_group: String,
                                     xml_booking_login: String)

  case class BookRequestErrorsCount(errors_count: Long,
                                    time: String,
                                    brand_name: String,
                                    sales_channel: String,
                                    trade_group: String,
                                    trade_name: String,
                                    trade_parent_group: String,
                                    xml_booking_login: String)

  case class BookRequestResponseTime(time: String,
                                     brand_name: String,
                                     sales_channel: String,
                                     trade_group: String,
                                     trade_name: String,
                                     trade_parent_group: String,
                                     xml_booking_login: String,
                                     min_response_time_ms: Long,
                                     max_response_time_ms: Long,
                                     perc_response_time_ms: Double)

  case class BookRequestCountOpt(book_count: Option[Long] = None,
                                 time: Option[String] = None,
                                 brand_name: Option[String] = None,
                                 sales_channel: Option[String] = None,
                                 trade_group: Option[String] = None,
                                 trade_name: Option[String] = None,
                                 trade_parent_group: Option[String] = None,
                                 xml_booking_login: Option[String] = None)

}
