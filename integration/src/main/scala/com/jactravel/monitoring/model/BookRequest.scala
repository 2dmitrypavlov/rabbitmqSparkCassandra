package com.jactravel.monitoring.model

import org.glassfish.jersey.internal.Errors.ErrorMessage

/**
  * Created by admin on 6/10/17.
  */
case class BookRequest(
                        queryUUID: String,
                        searchQueryUUID: String,
                        preBookQueryUUID: String,
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