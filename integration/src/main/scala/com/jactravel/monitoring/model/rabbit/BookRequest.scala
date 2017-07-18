package com.jactravel.monitoring.model.rabbit

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
