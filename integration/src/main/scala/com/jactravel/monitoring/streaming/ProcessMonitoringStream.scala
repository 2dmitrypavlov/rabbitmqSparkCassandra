package com.jactravel.monitoring.streaming

import com.jactravel.monitoring.client.search.Clientsearch
import com.jactravel.monitoring.model.ClientSearch
import com.rabbitmq.client.QueueingConsumer.Delivery
import com.typesafe.scalalogging.LazyLogging

import scala.collection.JavaConverters._

/**
  * Created by eugen on 5/30/17.
  */
trait ProcessMonitoringStream extends LazyLogging {

  def messageHandler(delivery: Delivery) : ClientSearch = {
    logger.info(new String(delivery.getBody))
    val clientSearchProto = Clientsearch.clientsearch.parseFrom(delivery.getBody)

    ClientSearch(SearchQueryUUID = "123")
    // return ClientSearch object
    /*ClientSearch(SearchQueryUUID = clientSearchProto.getSearchQueryUUID
    , ClientIP = clientSearchProto.getClientIP
    , Host = clientSearchProto.getHost
    , ClientRequestTimestamp = clientSearchProto.getClientRequestTimestamp
    , ClientResponseTimestamp = clientSearchProto.getClientResponseTimestamp
    , ForwardedRequestTimestamp = clientSearchProto.getForwardedRequestTimestamp
    , ForwardedResponseTimestamp = clientSearchProto.getForwardedResponseTimestamp
    , TradeID = clientSearchProto.getTradeID
    , BrandID = clientSearchProto.getBrandID
    , SalesChannelID = clientSearchProto.getSalesChannelID
    , GeographyLevel1ID = clientSearchProto.getGeographyLevel1ID
    , GeographyLevel2ID = clientSearchProto.getGeographyLevel2ID
    , GeographyLevel3ID = clientSearchProto.getGeographyLevel3IDList.asScala
    , PropertyID = clientSearchProto.getPropertyIDList.asScala
    , PropertyReferenceID = clientSearchProto.getPropertyReferenceIDList.asScala
    , ArrivalDate = clientSearchProto.getArrivalDate
    , Duration = clientSearchProto.getDuration
    , Rooms = clientSearchProto.getRooms
    , Adults = clientSearchProto.getAdultsList.asScala
    , Children = clientSearchProto.getChildrenList.asScala
    , ChildAges = clientSearchProto.getChildAgesList.asScala
    , MealBasisID = clientSearchProto.getMealBasisID
    , MinStarRating = clientSearchProto.getMinStarRating
    , HotelCount = clientSearchProto.getHotelCount
    , Success = clientSearchProto.getSuccess
    , ErroMessage = clientSearchProto.getErroMessage
    , SuppliersSearched = clientSearchProto.getSuppliersSearched
    , RequestXML = clientSearchProto.getRequestXML
    , ResponseXML = clientSearchProto.getResponseXML)*/


  }

}
