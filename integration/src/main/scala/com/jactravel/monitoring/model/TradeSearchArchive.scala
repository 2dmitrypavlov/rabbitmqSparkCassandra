package com.jactravel.monitoring.model

import java.sql.Date

/**
  * Created by admin on 7/7/17.
  */
case class TradeSearchArchive(
                               searchDate: Date,
                               tradeId: Int,
                               xmlBookLogin: String,
                               searchHour: Int,
                               requestProcessor: String,
                               brandId: Int,
                               salesChannelId: Int,
                               searchGeoLevel: String,
                               geoLevel1Id: Int,
                               geoLevel2Id: Int,
                               arrivalDate: Date,
                               duration: Int,
                               adults: Int,
                               children: Int,
                               success: String,
                               propertyReferenceCount: Int, // Sum of all propertyReferenceCount
                               propertyCount: Int,
                               pricedRoomCount: Int,
                               totalTime: Long, // Sum of all QueryProxyTime (clientResponseUtcTimestamp - clientRequestUtcTimestamp)
                               totalTimeP50: Long, // 50% percentile (clientResponseUtcTimestamp - clientRequestUtcTimestamp)
                               totalTimeP95: Long, // 95% percentile (clientResponseUtcTimestamp - clientRequestUtcTimestamp)
                               totalTimeP99: Long, // 99% percentile (clientResponseUtcTimestamp - clientRequestUtcTimestamp)
                               processingTime: Long, // Sum of all SearchRequest times (endUtcTimestamp - startUtcTimestamp)
                               processingTimeP50: Long, // 50% percentile (endUtcTimestamp - startUtcTimestamp)
                               processingTimeP95: Long, // 95% percentile (endUtcTimestamp - startUtcTimestamp)
                               processingTimeP99: Long, // 99% percentile (endUtcTimestamp - startUtcTimestamp)
                               searchCount: Int

                             )