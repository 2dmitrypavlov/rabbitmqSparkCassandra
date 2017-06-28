package com.jactravel.monitoring.model

/**
  * Created by admin on 6/28/17.
  */
case class CmiBatchRequest(
                       queryUUID: String,
                       supplierIp: String,
                       cmiQueryType: Int,
                       host: String,
                       requestUtcTimestamp: String,
                       responseUtcTimestamp: String,
                       requestXml: String,
                       responseXml: String,
                       success: String,
                       errorMessage: String,
                       errorStackTrace: String
                     )

