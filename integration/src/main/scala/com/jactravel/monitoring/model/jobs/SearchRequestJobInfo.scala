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

//  def toSearchCountPoint(src: SearchRequestCount): Point = {
//    Point("search_request_count")
//      .addTag("mtime", src.time)
//      .addTag("brand_name", src.brand_name)
//      .addTag("sales_channel", src.sales_channel)
//      .addTag("trade_group", src.trade_group)
//      .addTag("trade_name", src.trade_name)
//      .addTag("trade_parent_group", src.trade_parent_group)
//      .addTag("xml_booking_login", src.xml_booking_login)
//      .addField("search_count", src.search_count)
//  }
//
//  def toSuccessCountPoint(srs: SearchRequestSuccess): Point = {
//    Point("search_success_count")
//      .addTag("mtime", srs.time)
//      .addTag("brand_name", srs.brand_name)
//      .addTag("sales_channel", srs.sales_channel)
//      .addTag("trade_group", srs.trade_group)
//      .addTag("trade_name", srs.trade_name)
//      .addTag("trade_parent_group", srs.trade_parent_group)
//      .addTag("xml_booking_login", srs.xml_booking_login)
//      .addField("success_count", srs.success_count)
//  }
//
//  def toErrorsCountPoint(sre: SearchRequestErrors): Point = {
//    Point("search_errors_count")
//      .addTag("mtime", sre.time)
//      .addTag("brand_name", sre.brand_name)
//      .addTag("sales_channel", sre.sales_channel)
//      .addTag("trade_group", sre.trade_group)
//      .addTag("trade_name", sre.trade_name)
//      .addTag("trade_parent_group", sre.trade_parent_group)
//      .addTag("xml_booking_login", sre.xml_booking_login)
//      .addField("errors_count", sre.errors_count)
//  }
//
//  def toResponseTimePoint(srrt: SearchRequestResponseTime): Point = {
//    Point("search_response_time")
//      .addTag("mtime", srrt.time)
//      .addTag("brand_name", srrt.brand_name)
//      .addTag("sales_channel", srrt.sales_channel)
//      .addTag("trade_group", srrt.trade_group)
//      .addTag("trade_name", srrt.trade_name)
//      .addTag("trade_parent_group", srrt.trade_parent_group)
//      .addTag("xml_booking_login", srrt.xml_booking_login)
//      .addField("min_response_time", srrt.min_response_time_ms)
//      .addField("max_response_time", srrt.max_response_time_ms)
//      .addField("perc_response_time", srrt.perc_response_time_ms)
//  }
}
