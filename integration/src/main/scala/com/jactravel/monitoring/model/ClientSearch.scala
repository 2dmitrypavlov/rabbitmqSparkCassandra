package com.jactravel.monitoring.model

/**
  * Created by admin on 5/30/17.
  */
case class ClientSearch (SearchQueryUUID: String
                         , ClientIP: String
                         , Host: String
                         , ClientRequestTimestamp: String
                         , ClientResponseTimestamp: String
                         , ForwardedRequestTimestamp: String
                         , ForwardedResponseTimestamp: String
                         , TradeID: Int
                         , BrandID: Int
                         , SalesChannelID: Int
                         , GeographyLevel1ID: Int
                         , GeographyLevel2ID: Int
                         , GeographyLevel3ID: Int
                         , PropertyID: Int
                         , PropertyReferenceID: Int
                         , ArrivalDate: String
                         , Duration: Int
                         , Rooms: Int
                         , Adults: Int
                         , Children: Int
                         , ChildAges: Int
                         , MealBasisID: Int
                         , MinStarRating: String
                         , HotelCount: Int
                         , Success: String
                         , ErroMessage: String
                         , SuppliersSearched: Int
                         , RequestXML: String
                         , ResponseXML: String)
