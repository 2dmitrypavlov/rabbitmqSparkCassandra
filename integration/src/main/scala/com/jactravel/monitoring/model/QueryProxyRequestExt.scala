package com.jactravel.monitoring.model

/**
  * Created by admin on 7/6/17.
  */
case class QueryProxyRequestExt(
                                 queryUuid: String,
                                 brandName: Option[String] = None,
                                 tradeName: Option[String] = None,
                                 tradeGroup: Option[String] = None,
                                 tradeGroupParent: Option[String] = None,
                                 salesChannel: Option[String] = None,
                                 deltaRequestTime: Option[Long] = None,
                                 errorStackTrace: Option[String] = None,
                                 success: Option[String] = None,
                                 xmlBookingLogin: Option[String] = None,
                                 timeIn: Option[Long] = None,
                                 tableName: Option[String] = None
                               ) {
  override def toString: String = {
    s"[QueryUUID = $queryUuid, brandName = $brandName, tradeName = $tradeName, timeIn = $timeIn]"
  }
}
