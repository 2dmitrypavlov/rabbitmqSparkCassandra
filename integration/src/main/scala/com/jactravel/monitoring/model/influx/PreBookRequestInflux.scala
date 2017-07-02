package com.jactravel.monitoring.model.influx

import org.joda.time.DateTime

/**
  * Created by fayaz on 30.06.17.
  */
object PreBookRequestInflux {

  /**
    *  Use this class for entity type (count, success, failure),
    *  they are the same, just change influx measurement.
    */
  case class PreBookRequestCount(count: Long,
                                 time: DateTime,
                                 brandName: String,
                                 salesChannel: String,
                                 tradeGroup: String,
                                 tradeName: String,
                                 tradeParentGroup: String,
                                 xmlBookingLogin: String)

  case class PreBookRequesrResponseTime(time: DateTime,
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
