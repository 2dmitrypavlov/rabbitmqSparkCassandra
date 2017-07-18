//package com.jactravel.monitoring.model.tmp
//
//import com.jactravel.monitoring.model.rabbit.BookRoomInfo
//import org.joda.time.DateTime
//
///**
//  * Created by admin on 6/10/17.
//  */
//case class BookRequestTime(
//                            queryUUID: String,
//                            requestUtcTimestamp: DateTime,
//                            searchQueryUUID: String,
//                            preBookQueryUUID: String,
//                            searchProcessor: Int,
//                            host: String,
//                            startUtcTimestamp: String,
//                            endUtcTimestamp: String,
//                            tradeId: Int,
//                            brandId: Int,
//                            salesChannelId: Int,
//                            propertyId: Int,
//                            arrivalDate: String,
//                            duration: Int,
//                            rooms: List[BookRoomInfo],
//                            currencyId: Int,
//                            success: String,
//                            errorMessage: String,
//                            errorStackTrace: String
//                      )