package com.jactravel.monitoring.model.rabbit

/**
  * Created by dmitry on 7/18/17.
  */
case class CmiBatchRequest2(
                           querySecond:Long,
                            queryUUID: String,
                            supplierIp: String,
                            cmiQueryType: Int,
                            host: String,
                            requestUtcTimestamp: String,
                            responseUtcTimestamp: String,
                            requestXml: String,
                            responseXml: String,
                            login: String,
                            propertyCode: String,
                            success: String,
                            errorMessage: String,
                            errorStackTrace: String
                          )
