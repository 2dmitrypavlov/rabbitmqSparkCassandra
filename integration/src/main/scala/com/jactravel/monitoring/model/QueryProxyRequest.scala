package com.jactravel.monitoring.model

import java.util.Date

/**
  * Created by admin on 6/13/17.
  */
case class QueryProxyRequest(
                              queryUUID: String,
                              requestUtcTimestamp: Date,
                              clientIP: String,
                              searchQueryType: Int,
                              host: String,
                              clientRequestUtcTimestamp: Date,
                              clientResponseUtcTimestamp: Date,
                              forwardedRequestUtcTimestamp: Date,
                              forwardedResponseUtcTimestamp: Date,
                              requestXML: String,
                              responseXML: String,
                              xmlBookingLogin: String,
                              success: String,
                              errorMessage: String,
                              requestProcessor: Int,
                              requestURL: String,
                              errorStackTrace: String,
                              currentTimeInMillis: Long
                            )
