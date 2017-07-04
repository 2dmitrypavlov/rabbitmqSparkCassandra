package com.jactravel.monitoring.model.influx



/**
  * Created by fayaz on 30.06.17.
  */
object BookRequestInflux {

  /**
    *  Use this class for entity type (count, success, failure),
    *  they are the same, just change influx measurement.
    */


  case class BookRequestCount(booking_count: Long,
                              tm: String,
                              brand_name: String,
                              sales_channel: String,
                              trade_group: String,
                              trade_name: String,
                              trade_parent_group: String,
                              xml_booking_login: String)

  case class BookRequestResponseTime(time: String,
                                     brandName: String,
                                     salesChannel: String,
                                     tradeGroup: String,
                                     tradeName: String,
                                     tradeParentGroup: String,
                                     xmlBookingLogin: String,
                                     minResponseTime: Long,
                                     maxResponseTime: Long,
                                     averageResponseTime: Long)

}
