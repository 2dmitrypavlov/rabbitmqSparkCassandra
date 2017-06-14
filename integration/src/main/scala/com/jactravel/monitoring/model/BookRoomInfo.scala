package com.jactravel.monitoring.model

/**
  * Created by admin on 6/13/17.
  */
case class BookRoomInfo(
                         adults: Int,
                         children: Int,
                         childAges: List[Int],
                         mealBasisID: Int,
                         bookingToken: String,
                         propertyRoomTypeID: Int,
                         priceDiff: String
                       )
