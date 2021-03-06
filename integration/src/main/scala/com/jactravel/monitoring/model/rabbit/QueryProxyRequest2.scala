package com.jactravel.monitoring.model.rabbit

/**
  * Created by dmitry on 7/18/17.
  */
case class QueryProxyRequest2(
                              queryUUID: String,
                              clientIp: String,
                              searchQueryType: Int,
                              host: String,
                              clientRequestUtcTimestamp: java.sql.Date,
                              clientResponseUtcTimestamp: java.sql.Date,
                              forwardedRequestUtcTimestamp: java.sql.Date,
                              forwardedResponseUtcTimestamp: java.sql.Date,
                              requestXml: String,
                              responseXml: String,
                              xmlBookingLogin: String,
                              success: String,
                              errorMessage: String,
                              requestProcessor: Int,
                              requestURL: String,
                              errorStackTrace: String
                            )
