package com.jactravel.monitoring.model

/**
  * Created by admin on 6/28/17.
  */
case class CmiRequest(
                       queryUUID: String,
                       supplierIp: String,
                       cmiQueryType: Int,
                       host: String,
                       clientRequestUtcTimestamp: String,
                       clientResponseUtcTimestamp: String,
                       forwardedRequestUtcTimestamp: String,
                       forwardedResponseUtcTimestamp: String,
                       requestXml: String,
                       responseXml: String,
                       xmlBookingLogin: String,
                       success: String,
                       errorMessage: String,
                       requestProcessor: Int,
                       requestURL: String,
                       errorStackTrace: String
                     )

case class CmiRequest2(
                       querySecond: Long,
                       queryUUID: String,
                       supplierIp: String,
                       cmiQueryType: Int,
                       host: String,
                       clientRequestUtcTimestamp: String,
                       clientResponseUtcTimestamp: String,
                       forwardedRequestUtcTimestamp: String,
                       forwardedResponseUtcTimestamp: String,
                       requestXml: String,
                       responseXml: String,
                       xmlBookingLogin: String,
                       success: String,
                       errorMessage: String,
                       requestProcessor: Int,
                       requestURL: String,
                       errorStackTrace: String
                     )
