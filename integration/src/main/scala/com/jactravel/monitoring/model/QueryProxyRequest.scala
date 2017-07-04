package com.jactravel.monitoring.model

import java.util.Date;

/**
  * Created by admin on 6/13/17.
  */
case class QueryProxyRequest(
                              queryUUID: String,
                              clientIp: String,
                              searchQueryType: Int,
                              host: String,
                              clientRequestUtcTimestamp: Date,
                              clientResponseUtcTimestamp: Date,
                              forwardedRequestUtcTimestamp: Date,
                              forwardedResponseUtcTimestamp: Date,
                              requestXml: String,
                              responseXml: String,
                              xmlBookingLogin: String,
                              success: String,
                              errorMessage: String,
                              requestProcessor: Int,
                              requestURL: String,
                              errorStackTrace: String
                            )
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