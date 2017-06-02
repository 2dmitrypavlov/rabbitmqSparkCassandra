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

    val clientSearchProto = Clientsearch.clientsearch.parseFrom(delivery.getBody)

    //ClientSearch(SearchQueryUUID = "123")
    // return ClientSearch object
    val clientSearch = ClientSearch(SearchQueryUUID = clientSearchProto.getSearchQueryUUID
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
    , GeographyLevel3ID = clientSearchProto.getGeographyLevel3IDList
    , PropertyID = clientSearchProto.getPropertyIDList
    , PropertyReferenceID = clientSearchProto.getPropertyReferenceIDList
    , ArrivalDate = clientSearchProto.getArrivalDate
    , Duration = clientSearchProto.getDuration
    , Rooms = clientSearchProto.getRooms
    , Adults = clientSearchProto.getAdultsList
    , Children = clientSearchProto.getChildrenList
    , ChildAges = clientSearchProto.getChildAgesList
    , MealBasisID = clientSearchProto.getMealBasisID
    , MinStarRating = clientSearchProto.getMinStarRating
    , HotelCount = clientSearchProto.getHotelCount
    , Success = clientSearchProto.getSuccess
    , ErroMessage = clientSearchProto.getErroMessage
    , SuppliersSearched = clientSearchProto.getSuppliersSearched
    , RequestXML = clientSearchProto.getRequestXML
    , ResponseXML = clientSearchProto.getResponseXML)

    logger.info("-----------------------------------------------------------------------------------")
    logger.info("-----------------------------------------------------------------------------------")
    logger.info(s"SearchQueryUUID: ${clientSearch.SearchQueryUUID}")
    logger.info("-----------------------------------------------------------------------------------")
    logger.info("-----------------------------------------------------------------------------------")
    clientSearch

  }

}
