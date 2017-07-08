package com.jactravel.monitoring.model

/**
  * Created by admin on 6/10/17.
  */
case class PreBookRequest(
                           queryUUID: String,
                           searchQueryUUID: String,
                           searchProcessor: Int,
                           host: String,
                           startUtcTimestamp: String,
                           endUtcTimestamp: String,
                           tradeId: Int,
                           brandId: Int,
                           salesChannelId: Int,
                           propertyId: Int,
                           arrivalDate: String,
                           duration: Int,
                           rooms: List[BookRoomInfo],
                           currencyId: Int,
                           success: String,
                           errorMessage: String,
                           errorStackTrace: String
                         )

case class PreBookRequest2(
                           querySecond: Long,
                           queryUUID: String,
                           searchQueryUUID: String,
                           searchProcessor: Int,
                           host: String,
                           startUtcTimestamp: String,
                           endUtcTimestamp: String,
                           tradeId: Int,
                           brandId: Int,
                           salesChannelId: Int,
                           propertyId: Int,
                           arrivalDate: String,
                           duration: Int,
                           rooms: List[BookRoomInfo],
                           currencyId: Int,
                           success: String,
                           errorMessage: String,
                           errorStackTrace: String
                         )

case class SupplierPreBookRequest(
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

case class SupplierPreBookRequest2(
                                   querySecond: Long,
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
