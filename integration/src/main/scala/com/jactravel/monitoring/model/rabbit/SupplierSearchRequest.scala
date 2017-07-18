package com.jactravel.monitoring.model.rabbit

/**
  * Created by dmitry on 7/18/17.
  */
case class SupplierSearchRequest(
                                  queryUUID: String,
                                  host: String,
                                  source: String,
                                  startUtcTimestamp: String,
                                  endUtcTimestamp: String,
                                  timeout: Int,
                                  propertyCount: Int,
                                  success: String,
                                  errorMessage: String,
                                  errorStackTrace: String,
                                  requestXml: String,
                                  responseXml: String,
                                  requestCount: Int
                                )
