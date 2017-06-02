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
                         , GeographyLevel3ID: java.util.List[Integer]
                         , PropertyID: java.util.List[Integer]
                         , PropertyReferenceID: java.util.List[Integer]
                         , ArrivalDate: String = ""
                         , Duration: Int = 0
                         , Rooms: Int = 0
                         , Adults: java.util.List[Integer]
                         , Children: java.util.List[Integer]
                         , ChildAges: java.util.List[Integer]
                         , MealBasisID: Int = 0
                         , MinStarRating: String = ""
                         , HotelCount: Int = 0
                         , Success: String = ""
                         , ErroMessage: String = ""
                         , SuppliersSearched: Int = 0
                         , RequestXML: String = ""
                         , ResponseXML: String = "")
