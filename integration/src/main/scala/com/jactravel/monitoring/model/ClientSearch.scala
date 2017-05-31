package com.jactravel.monitoring.model

/**
  * Created by admin on 5/30/17.
  */
case class ClientSearch (SearchQueryUUID: String
                         , ClientIP: String = ""
                         , Host: String = ""
                         , ClientRequestTimestamp: String = ""
                         , ClientResponseTimestamp: String = ""
                         , ForwardedRequestTimestamp: String = ""
                         , ForwardedResponseTimestamp: String = ""
                         , TradeID: Int = 0
                         , BrandID: Int = 0
                         , SalesChannelID: Int = 0
                         , GeographyLevel1ID: Int = 0
                         , GeographyLevel2ID: Int = 0
                         , GeographyLevel3ID: Int = 0
                         , PropertyID: Int = 0
                         , PropertyReferenceID: Int = 0
                         , ArrivalDate: String = ""
                         , Duration: Int = 0
                         , Rooms: Int = 0
                         , Adults: Int = 0
                         , Children: Int = 0
                         , ChildAges: Int = 0
                         , MealBasisID: Int = 0
                         , MinStarRating: String = ""
                         , HotelCount: Int = 0
                         , Success: String = ""
                         , ErroMessage: String = ""
                         , SuppliersSearched: Int = 0
                         , RequestXML: String = ""
                         , ResponseXML: String = "")
