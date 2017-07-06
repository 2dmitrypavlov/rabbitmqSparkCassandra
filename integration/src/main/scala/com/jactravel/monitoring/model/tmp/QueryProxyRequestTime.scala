package com.jactravel.monitoring.model.tmp

import org.joda.time.DateTime

/**
  * Created by admin on 6/13/17.
  */
case class QueryProxyRequestTime(
                              queryUUID: String,
                              requestUtcTimestamp: DateTime,
                              clientIp: String,
                              searchQueryType: Int,
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