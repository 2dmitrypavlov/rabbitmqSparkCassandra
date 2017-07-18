package com.jactravel.monitoring.model.rabbit

/**
  * Created by dmitry on 7/18/17.
  */
case class RequestInfo(
                              startUtcTimestamp: String,
                              endUtcTimestamp: String,
                              tradeId: Int,
                              brandId: Int,
                              salesChannelId: Int,
                              searchGeoLevel: Int,
                              geo_level1_id: Int,
                              geo_level2_id: Int,
                              geo_level3_ids: List[Int],
                              property_reference_ids: List[Int],
                              property_ids: List[Int],
                              minStarRating: String,
                              arrivalDate: String,
                              duration: Int,
                              mealBasisId: Int,
                              rooms: List[RoomRequest],
                              room_count: Int
                            )
