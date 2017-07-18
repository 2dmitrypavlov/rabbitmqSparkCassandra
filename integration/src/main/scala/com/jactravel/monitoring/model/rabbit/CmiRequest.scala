package com.jactravel.monitoring.model.rabbit

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
                       login: String,
                       propertyCode: String,
                       success: String,
                       errorMessage: String,
                       requestProcessor: Int,
                       requestURL: String,
                       errorStackTrace: String
                     )
