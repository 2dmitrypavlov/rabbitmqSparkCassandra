package com.jactravel.monitoring.model.rabbit

/**
  * Created by dmitry on 7/18/17.
  */
case class BookRequest2(querySecond: Long,
                        queryUUID: String,
                        searchQueryUUID: String,
                        preBookQueryUUID: String,
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
