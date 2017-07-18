package com.jactravel.monitoring.model.jobs

import com.paulgoldbaum.influxdbclient.Point

/**
  * Created by fayaz on 09.07.17.
  */
object SupplierBookRequestJobInfo {
  case class SupplierBookRequestInfo(
                                      success_rate: Double,
                                      book_count: Long,
                                      processing_time_ms_50: Double,
                                      processing_time_ms_95: Double,
                                      processing_time_ms_99: Double,
                                      source: String,
                                      xml_booking_login: String,
                                      trade_name: String,
                                      brand_name: String,
                                      sales_channel: String
                                    )

//  def toPoint(spbri: SupplierBookRequestInfo): Point = {
//    Point("supplier_book_request")
//      .addTag("source", spbri.source)
//      .addTag("xml_booking_login", spbri.xml_booking_login)
//      .addTag("trade_name", spbri.trade_name)
//      .addTag("brand_name", spbri.brand_name)
//      .addTag("sales_channel", spbri.sales_channel)
//      .addField("pre_book_count", spbri.book_count)
//      .addField("processing_time_ms_50", spbri.processing_time_ms_50)
//      .addField("processing_time_ms_95", spbri.processing_time_ms_95)
//      .addField("processing_time_ms_99", spbri.processing_time_ms_99)
//
//  }
}
