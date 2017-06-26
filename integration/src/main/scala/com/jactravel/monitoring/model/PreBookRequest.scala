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
                           tradeID: Int,
                           brandID: Int,
                           salesChannelID: Int,
                           propertyID: Int,
                           arrivalDate: String,
                           duration: Int,
                           rooms: List[BookRoomInfo],
                           currencyID: Int,
                           preBookingToken: String,
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
                                errorStackTrace: String
                              )
