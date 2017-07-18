package com.jactravel.monitoring.model.rabbit

/**
  * Created by dmitry on 7/18/17.
  */
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
                       login: String,
                       propertyCode: String,
                       success: String,
                       errorMessage: String,
                       requestProcessor: Int,
                       requestURL: String,
                       errorStackTrace: String
                     )
