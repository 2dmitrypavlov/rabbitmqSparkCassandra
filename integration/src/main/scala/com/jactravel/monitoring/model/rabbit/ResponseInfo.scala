package com.jactravel.monitoring.model.rabbit

/**
  * Created by dmitry on 7/18/17.
  */
case class ResponseInfo(
                               propertyReferenceCount: Int,
                               propertyCount: Int,
                               pricedRoomCount: Int,
                               suppliersSearched: List[String],
                               success: String,
                               errorMessage: String,
                               errorStackTrace: String
                             )
