package com.jactravel.monitoring.model.rabbit

/**
  * Created by dmitry on 7/18/17.
  */
case class SearchRequest2(
                          querySecond: Long,
                          queryUUID: String,
                          host: String,
                          requestInfo: RequestInfo,
                          responseInfo: ResponseInfo
                        )
