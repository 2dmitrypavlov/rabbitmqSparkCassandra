package com.jactravel.monitoring.model

/**
  * Created by admin on 7/6/17.
  */

trait CommonRequest {
                                 def queryUuid: String
                                 def brandName: Option[String] = None
                                 def tradeName: Option[String] = None
                                 def tradeGroup: Option[String] = None
                                 def tradeGroupParent: Option[String] = None
                                 def salesChannel: Option[String] = None
                                 def deltaRequestTime: Option[Long] = None
                                 def errorStackTrace: Option[String] = None
                                 def success: Option[String] = None
                                 def xmlBookingLogin: Option[String] = None
                                 def timeIn: Option[Long] = None

  override def toString: String = {
    s"[QueryUUID = $queryUuid, brandName = $brandName, tradeName = $tradeName, timeIn = $timeIn]"
  }
}

case class QueryProxyRequestExt(
                                 override val queryUuid: String,
                                 tableName: Option[String] = None
                               ) extends CommonRequest

case class RichBookRequest ( override val queryUuid: String ) extends CommonRequest
