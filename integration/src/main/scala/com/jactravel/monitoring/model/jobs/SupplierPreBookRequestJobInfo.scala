package com.jactravel.monitoring.model.jobs

import com.paulgoldbaum.influxdbclient.Point

/**
  * Created by fayaz on 09.07.17.
  */
object SupplierPreBookRequestJobInfo {
  case class SupplierPreBookRequestInfo(
                                         pre_book_count: Long,
                                         processing_time_ms_50: Double,
                                         processing_time_ms_95: Double,
                                         processing_time_ms_99: Double,
                                         source: String,
                                         xml_booking_login: String,
                                         trade_name: String,
                                         brand_name: String,
                                         sales_channel: String
                                       )

}
