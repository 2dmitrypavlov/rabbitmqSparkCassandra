package com.jactravel.monitoring.model

/**
  * Created by admin on 6/13/17.
  */
case class SearchRequestInfo(
                              queryUUID: String,
                              host: String,
                              startUtcTimestamp: String,
                              endUtcTimestamp: String,
                              tradeID: Int,
                              brandID: Int,
                              salesChannelID: Int,
                              searchGeoLevel: Int,
                              geo_level1_id: Int,
                              geo_level2_id: Int,
                              geo_level3_id: List[Int],
                              property_reference_ids: List[Int],
                              property_ids: List[Int],
                              minStarRating: String,
                              arrivalDate: String,
                              duration: Int,
                              mealBasisID: Int,
                              rooms: List[RoomRequest],
                              errorMessage: String,
                              errorStackTrace: String
                            )
