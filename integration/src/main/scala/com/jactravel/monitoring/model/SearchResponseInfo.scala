package com.jactravel.monitoring.model

/**
  * Created by admin on 6/13/17.
  */
case class SearchResponseInfo(
                               queryUUID: String,
                               host: String,
                               propertyReferenceCount: Int,
                               propertyCount: Int,
                               pricedRoomCount: Int,
                               success: String,
                               errorMessage: String,
                               suppliersSearched: List[String]
                             )
