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

  case class BookRequestSuccessCountOpt(success_count: Option[Long] = None,
                                        time: Option[String] = None,
                                        brand_name: Option[String] = None,
                                        sales_channel: Option[String] = None,
                                        trade_group: Option[String] = None,
                                        trade_name: Option[String] = None,
                                        trade_parent_group: Option[String] = None,
                                        xml_booking_login: Option[String] = None)

  case class BookRequestErrorsCountOpt(
                                     errors_count: Option[Long] = None,
                                     time: Option[String] = None,
                                     brand_name: Option[String] = None,
                                     sales_channel: Option[String] = None,
                                     trade_group: Option[String] = None,
                                     trade_name: Option[String] = None,
                                     trade_parent_group: Option[String] = None,
                                     xml_booking_login: Option[String] = None
                                   )

  case class BookRequestResponseTimeOpt(
                                      time: Option[String] = None,
                                      brand_name: Option[String] = None,
                                      sales_channel: Option[String] = None,
                                      trade_group: Option[String] = None,
                                      trade_name: Option[String] = None,
                                      trade_parent_group: Option[String] = None,
                                      xml_booking_login: Option[String] = None,
                                      min_response_time_ms: Option[Long] = None,
                                      max_response_time_ms: Option[Long] = None,
                                      perc_response_time_ms: Option[Double] = None
                                    )

  def toBookCountPoint(brc: BookRequestCountOpt): Point = {
    Point("book_request_count")
      .addTag("mtime", brc.time.getOrElse("NoneValue"))
      .addTag("brand_name", brc.brand_name.getOrElse("NoneValue"))
      .addTag("sales_channel", brc.sales_channel.getOrElse("NoneValue"))
      .addTag("trade_group", brc.trade_group.getOrElse("NoneValue"))
      .addTag("trade_name", brc.trade_name.getOrElse("NoneValue"))
      .addTag("trade_parent_group", brc.trade_parent_group.getOrElse("NoneValue"))
      .addTag("xml_booking_login", brc.xml_booking_login.getOrElse("NoneValue"))
      .addField("book_count", brc.book_count.getOrElse(-1L))
  }

  def toSuccessCountPoint(brsc: BookRequestSuccessCountOpt): Point = {
    Point("book_success_count")
      .addTag("mtime", brsc.time.getOrElse("NoneValue"))
      .addTag("brand_name", brsc.brand_name.getOrElse("NoneValue"))
      .addTag("sales_channel", brsc.sales_channel.getOrElse("NoneValue"))
      .addTag("trade_group", brsc.trade_group.getOrElse("NoneValue"))
      .addTag("trade_name", brsc.trade_name.getOrElse("NoneValue"))
      .addTag("trade_parent_group", brsc.trade_parent_group.getOrElse("NoneValue"))
      .addTag("xml_booking_login", brsc.xml_booking_login.getOrElse("NoneValue"))
      .addField("success_count", brsc.success_count.getOrElse(-1L))
  }

  def toErrorsCountPoint(brec: BookRequestErrorsCountOpt): Point = {
    Point("book_errors_count")
      .addTag("mtime", brec.time.getOrElse("NoneValue"))
      .addTag("brand_name", brec.brand_name.getOrElse("NoneValue"))
      .addTag("sales_channel", brec.sales_channel.getOrElse("NoneValue"))
      .addTag("trade_group", brec.trade_group.getOrElse("NoneValue"))
      .addTag("trade_name", brec.trade_name.getOrElse("NoneValue"))
      .addTag("trade_parent_group", brec.trade_parent_group.getOrElse("NoneValue"))
      .addTag("xml_booking_login", brec.xml_booking_login.getOrElse("NoneValue"))
      .addField("errors_count", brec.errors_count.getOrElse(-1L))
  }

  def toResponseTimePoint(brrt: BookRequestResponseTimeOpt): Point = {
    Point("book_response_time")
      .addTag("mtime", brrt.time.getOrElse("NoneValue"))
      .addTag("brand_name", brrt.brand_name.getOrElse("NoneValue"))
      .addTag("sales_channel", brrt.sales_channel.getOrElse("NoneValue"))
      .addTag("trade_group", brrt.trade_group.getOrElse("NoneValue"))
      .addTag("trade_name", brrt.trade_name.getOrElse("NoneValue"))
      .addTag("trade_parent_group", brrt.trade_parent_group.getOrElse("NoneValue"))
      .addTag("xml_booking_login", brrt.xml_booking_login.getOrElse("NoneValue"))
      .addField("min_response_time", brrt.min_response_time_ms.getOrElse(-1L))
      .addField("max_response_time", brrt.max_response_time_ms.getOrElse(-1L))
      .addField("perc_response_time", brrt.perc_response_time_ms.getOrElse(-1.0))
  }
}
