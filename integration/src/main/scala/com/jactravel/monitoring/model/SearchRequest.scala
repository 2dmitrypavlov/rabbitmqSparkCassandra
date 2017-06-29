package com.jactravel.monitoring.model

/**
  * Created by admin on 6/13/17.
  */

case class SearchRequest(
                          queryUUID: String,
                          host: String,
                          requestInfo: RequestInfo,
                          responseInfo: ResponseInfo
                        )

case class RoomRequest(
                        adults: Int,
                        children: Int,
                        childAges: List[Int]
                      )

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

case class ResponseInfo(
                               propertyReferenceCount: Int,
                               propertyCount: Int,
                               pricedRoomCount: Int,
                               suppliersSearched: List[String],
                               success: String,
                               errorMessage: String,
                               errorStackTrace: String
                             )

case class SupplierSearchRequest(
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
