package com.jactravel.monitoring.model

/**
  * Created by admin on 6/13/17.
  */
case class BookRoomInfo(
                         adults: Int,
                         children: Int,
                         childAges: List[Int],
                         mealBasisId: Int,
                         bookingToken: String,
                         propertyRoomTypeId: Int,
                         priceDiff: String,
                         roomCount: Int,
                         preBookingToken: String
                       )
