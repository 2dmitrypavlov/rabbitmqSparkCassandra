package com.jactravel.monitoring.model.tmp

import java.util.Date

import com.jactravel.monitoring.model.{RequestInfo, ResponseInfo}

/**
  * Created by admin on 6/13/17.
  */

case class SearchRequestTime(
                          queryUUID: String,
                          requestUtcTimestamp: Date,
                          host: String,
                          requestInfo: RequestInfo,
                          responseInfo: ResponseInfo
                        )
