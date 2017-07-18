package com.jactravel.monitoring.model.rabbit

/**
  * Created by admin on 6/13/17.
  */

case class SearchRequest(
                          queryUUID: String,
                          host: String,
                          requestInfo: RequestInfo,
                          responseInfo: ResponseInfo
                        )
