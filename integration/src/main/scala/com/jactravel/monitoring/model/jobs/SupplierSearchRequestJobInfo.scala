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

  def toPoint(ssri: SupplierSearchRequestInfo): Point = {
    Point("supplier_search_request")
      .addTag("mtime", ssri.time)
      .addTag("source", ssri.source)
      .addTag("brand_name", ssri.brand_name)
      .addTag("trade_group", ssri.trade_group)
      .addTag("trade_name", ssri.trade_name)
      .addTag("trade_parent_group", ssri.trade_parent_group)
      .addTag("xml_booking_login", ssri.xml_booking_login)
      .addField("client_search_сount", ssri.client_search_number)
      .addField("min_response_time", ssri.min_response_time)
      .addField("max_response_time", ssri.max_response_time)
      .addField("avg_response_time", ssri.avg_response_time)
      .addField("search_timeout_count", ssri.search_timeout_number)
      .addField("min_property_сount", ssri.min_property_number)
      .addField("avg_property_сount", ssri.avg_property_number)
      .addField("max_property_сount", ssri.max_property_number)
      .addField("success_count", ssri.success_number)
      .addField("error_count", ssri.error_number)
      .addField("search_count", ssri.search_number)

  }
}
