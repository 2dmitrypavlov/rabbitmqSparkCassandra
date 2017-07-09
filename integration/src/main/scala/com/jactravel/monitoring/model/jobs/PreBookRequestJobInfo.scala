package com.jactravel.monitoring.model.jobs

import com.paulgoldbaum.influxdbclient.Point

/**
  * Created by fayaz on 09.07.17.
  */
trait PreBookRequestJobInfo {
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

  def toPreBookCountPoint(brc: PreBookRequestCount): Point = {
    Point("pre_book_request_count")
      .addTag("mtime", brc.time)
      .addTag("brand_name", brc.brand_name)
      .addTag("sales_channel", brc.sales_channel)
      .addTag("trade_group", brc.trade_group)
      .addTag("trade_name", brc.trade_name)
      .addTag("trade_parent_group", brc.trade_parent_group)
      .addTag("xml_booking_login", brc.xml_booking_login)
      .addField("pre_book_count", brc.pre_book_count)
  }

  def toSuccessCountPoint(brsc: PreBookRequestSuccessCount): Point = {
    Point("pre_book_success_count")
      .addTag("mtime", brsc.time)
      .addTag("brand_name", brsc.brand_name)
      .addTag("sales_channel", brsc.sales_channel)
      .addTag("trade_group", brsc.trade_group)
      .addTag("trade_name", brsc.trade_name)
      .addTag("trade_parent_group", brsc.trade_parent_group)
      .addTag("xml_booking_login", brsc.xml_booking_login)
      .addField("success_count", brsc.success_count)
  }

  def toErrorsCountPoint(brec: PreBookRequestErrorsCount): Point = {
    Point("pre_book_errors_count")
      .addTag("mtime", brec.time)
      .addTag("brand_name", brec.brand_name)
      .addTag("sales_channel", brec.sales_channel)
      .addTag("trade_group", brec.trade_group)
      .addTag("trade_name", brec.trade_name)
      .addTag("trade_parent_group", brec.trade_parent_group)
      .addTag("xml_booking_login", brec.xml_booking_login)
      .addField("errors_count", brec.errors_count)
  }

  def toPreBookResponseTimePoint(brrt: PreBookRequestResponseTime): Point = {
    Point("pre_book_response_time")
      .addTag("mtime", brrt.time)
      .addTag("brand_name", brrt.brand_name)
      .addTag("sales_channel", brrt.sales_channel)
      .addTag("trade_group", brrt.trade_group)
      .addTag("trade_name", brrt.trade_name)
      .addTag("trade_parent_group", brrt.trade_parent_group)
      .addTag("xml_booking_login", brrt.xml_booking_login)
      .addField("min_response_time", brrt.min_response_time_ms)
      .addField("max_response_time", brrt.max_response_time_ms)
      .addField("perc_response_time", brrt.perc_response_time_ms)
  }

}