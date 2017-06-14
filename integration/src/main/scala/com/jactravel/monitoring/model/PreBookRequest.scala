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
                           preBookingToken: String
                         )
