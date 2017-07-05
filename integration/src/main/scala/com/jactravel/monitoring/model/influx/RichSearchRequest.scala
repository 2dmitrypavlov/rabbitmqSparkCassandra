package com.jactravel.monitoring.model.influx

import java.time.LocalDateTime

import org.apache.spark.sql.execution.streaming.FileStreamSource.Timestamp

/**
  * Created by admin on 7/1/17.
  */
case class RichSearchRequest(queryUUID: String
                            , brandName: String = ""
                            , tradeId: Int
                            , tradeName: String = ""
                            , tradeGroup: String = ""
                            , traderParentGroup: String = ""
                            , salesChannel: String = ""
                            , responseTimeMillis: Long = 0L
                            , errorStackTrace: String = ""
                            , success: String = ""
                            , xmlBookingLogin: String = ""
                            , requestTime: LocalDateTime = null)
