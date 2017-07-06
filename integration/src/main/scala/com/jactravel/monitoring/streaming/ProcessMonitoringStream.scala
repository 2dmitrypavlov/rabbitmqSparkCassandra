package com.jactravel.monitoring.streaming

import java.time._
import java.util.Date

import com.jactravel.monitoring.model._
import com.jactravel.monitoring.model.tmp.{BookRequestTime, PreBookRequestTime, QueryProxyRequestTime, SearchRequestTime}
import com.jactravel.monitoring.util.DateTimeUtils
import com.rabbitmq.client.QueueingConsumer.Delivery
import com.typesafe.scalalogging.LazyLogging

import scala.collection.JavaConverters._

/**
  * Created by eugen on 5/30/17.
  */
trait ProcessMonitoringStream extends LazyLogging {

  def messageBookingHandler(delivery: Delivery): BookRequest = {

    val bookRequestProto = com.jactravel.monitoring.BookRequest.PARSER.parseFrom(delivery.getBody)

    BookRequest(
      bookRequestProto.getQueryUUID
      , bookRequestProto.getSearchQueryUUID
      , bookRequestProto.getPreBookQueryUUID
      , bookRequestProto.getSearchProcessor.getNumber
      , bookRequestProto.getHost
      , bookRequestProto.getStartUtcTimestamp
      , bookRequestProto.getEndUtcTimestamp
      , bookRequestProto.getTradeID
      , bookRequestProto.getBrandID
      , bookRequestProto.getSalesChannelID
      , bookRequestProto.getPropertyID
      , bookRequestProto.getArrivalDate
      , bookRequestProto.getDuration
      , getRoomsInfo(bookRequestProto.getRoomsList)
      , bookRequestProto.getCurrencyID
      , bookRequestProto.getSuccess
      , bookRequestProto.getErrorMessage
      , bookRequestProto.getErrorStackTrace)

  }

  // TODO: DELETE THIS
  def messageBookingTimeHandler(delivery: Delivery): BookRequestTime = {

    val bookRequestProto = com.jactravel.monitoring.BookRequest.PARSER.parseFrom(delivery.getBody)

    BookRequestTime(
      bookRequestProto.getQueryUUID
      , Date.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant)
      , bookRequestProto.getSearchQueryUUID
      , bookRequestProto.getPreBookQueryUUID
      , bookRequestProto.getSearchProcessor.getNumber
      , bookRequestProto.getHost
      , bookRequestProto.getStartUtcTimestamp
      , bookRequestProto.getEndUtcTimestamp
      , bookRequestProto.getTradeID
      , bookRequestProto.getBrandID
      , bookRequestProto.getSalesChannelID
      , bookRequestProto.getPropertyID
      , bookRequestProto.getArrivalDate
      , bookRequestProto.getDuration
      , getRoomsInfo(bookRequestProto.getRoomsList)
      , bookRequestProto.getCurrencyID
      , bookRequestProto.getSuccess
      , bookRequestProto.getErrorMessage
      , bookRequestProto.getErrorStackTrace)
  }

  def messagePreBookingTimeHandler(delivery: Delivery): PreBookRequestTime = {

    val preBookRequestProto = com.jactravel.monitoring.PreBookRequest.PARSER.parseFrom(delivery.getBody)

    PreBookRequestTime(
      preBookRequestProto.getQueryUUID
      , Date.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant)
      , preBookRequestProto.getSearchQueryUUID
      , preBookRequestProto.getSearchProcessor.getNumber
      , preBookRequestProto.getHost
      , preBookRequestProto.getStartUtcTimestamp
      , preBookRequestProto.getEndUtcTimestamp
      , preBookRequestProto.getTradeID
      , preBookRequestProto.getBrandID
      , preBookRequestProto.getSalesChannelID
      , preBookRequestProto.getPropertyID
      , preBookRequestProto.getArrivalDate
      , preBookRequestProto.getDuration
      , getRoomsInfo(preBookRequestProto.getRoomsList)
      , preBookRequestProto.getCurrencyID
      , preBookRequestProto.getSuccess
      , preBookRequestProto.getErrorMessage
      , preBookRequestProto.getErrorStackTrace)

  }

  def messageSearchRequestTimeHandler(delivery: Delivery): SearchRequestTime = {

    val searchRequestProto = com.jactravel.monitoring.SearchRequest.PARSER.parseFrom(delivery.getBody)
    val searchRequestInfo = searchRequestProto.getRequestInfo
    val searchResponseInfo = searchRequestProto.getResponseInfo

    val searchRequestInfoRes = RequestInfo(
      searchRequestInfo.getStartUtcTimestamp
      , searchRequestInfo.getEndUtcTimestamp
      , searchRequestInfo.getTradeID
      , searchRequestInfo.getBrandID
      , searchRequestInfo.getSalesChannelID
      , searchRequestInfo.getSearchGeoLevel.getNumber
      , searchRequestInfo.getGeoLevel1ID
      , searchRequestInfo.getGeoLevel2ID
      , searchRequestInfo.getGeoLevel3IDsList.asScala.map(_.asInstanceOf[Int]).toList
      , searchRequestInfo.getPropertyReferenceIDsList.asScala.map(_.asInstanceOf[Int]).toList
      , searchRequestInfo.getPropertyIDsList.asScala.map(_.asInstanceOf[Int]).toList
      , searchRequestInfo.getMinStarRating
      , searchRequestInfo.getArrivalDate
      , searchRequestInfo.getDuration
      , searchRequestInfo.getMealBasisID
      , searchRequestInfo.getRoomsList.asScala.map(rr => RoomRequest(
        rr.getAdults
        , rr.getChildren
        , rr.getChildAgesList.asScala.map(_.asInstanceOf[Int]).toList)).toList
      , searchRequestInfo.getRoomCount
    )

    val searchResponseInfoRes = ResponseInfo(
      searchResponseInfo.getPropertyReferenceCount
      , searchResponseInfo.getPropertyCount
      , searchResponseInfo.getPricedRoomCount
      , searchResponseInfo.getSuppliersSearchedList.asScala.toList
      , searchResponseInfo.getSuccess
      , searchResponseInfo.getErrorMessage
      , searchResponseInfo.getErrorStackTrace
    )

    SearchRequestTime(
      searchRequestProto.getQueryUUID
      , Date.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant)
      , searchRequestProto.getHost
      , searchRequestInfoRes
      , searchResponseInfoRes
    )

  }

  def messageQueryProxyTimeHandler(delivery: Delivery): QueryProxyRequestTime = {

    val queryProxyRequest = com.jactravel.monitoring.QueryProxyRequest.PARSER.parseFrom(delivery.getBody)

    QueryProxyRequestTime(
      queryProxyRequest.getQueryUUID
      , Date.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant)
      , queryProxyRequest.getClientIP
      , queryProxyRequest.getSearchQueryType.getNumber
      , queryProxyRequest.getHost
      , DateTimeUtils.parseDate(queryProxyRequest.getClientRequestUtcTimestamp)
      , DateTimeUtils.parseDate(queryProxyRequest.getClientResponseUtcTimestamp)
      , DateTimeUtils.parseDate(queryProxyRequest.getForwardedRequestUtcTimestamp)
      , DateTimeUtils.parseDate(queryProxyRequest.getForwardedResponseUtcTimestamp)
      , queryProxyRequest.getRequestXML
      , queryProxyRequest.getResponseXML
      , queryProxyRequest.getXmlBookingLogin
      , queryProxyRequest.getSuccess
      , queryProxyRequest.getErrorMessage
      , queryProxyRequest.getRequestProcessor.getNumber
      , queryProxyRequest.getRequestURL
      , queryProxyRequest.getErrorStackTrace
    )

  }

  //================================================================================================


  def messageSupplierBookRequestHandler(delivery: Delivery): SupplierBookRequest = {

    val supplierBookRequestProto = com.jactravel.monitoring.SupplierBookRequest.PARSER.parseFrom(delivery.getBody)

    SupplierBookRequest(
      supplierBookRequestProto.getQueryUUID
      , supplierBookRequestProto.getHost
      , supplierBookRequestProto.getSource
      , supplierBookRequestProto.getStartUtcTimestamp
      , supplierBookRequestProto.getEndUtcTimestamp
      , supplierBookRequestProto.getTimeout
      , supplierBookRequestProto.getPropertyCount
      , supplierBookRequestProto.getSuccess
      , supplierBookRequestProto.getErrorMessage
      , supplierBookRequestProto.getErrorStackTrace
      , supplierBookRequestProto.getRequestXML
      , supplierBookRequestProto.getResponseXML
      , supplierBookRequestProto.getPropertyCount)

  }

  def messagePreBookingHandler(delivery: Delivery): PreBookRequest = {

    val preBookRequestProto = com.jactravel.monitoring.PreBookRequest.PARSER.parseFrom(delivery.getBody)

    PreBookRequest(
      preBookRequestProto.getQueryUUID
      , preBookRequestProto.getSearchQueryUUID
      , preBookRequestProto.getSearchProcessor.getNumber
      , preBookRequestProto.getHost
      , preBookRequestProto.getStartUtcTimestamp
      , preBookRequestProto.getEndUtcTimestamp
      , preBookRequestProto.getTradeID
      , preBookRequestProto.getBrandID
      , preBookRequestProto.getSalesChannelID
      , preBookRequestProto.getPropertyID
      , preBookRequestProto.getArrivalDate
      , preBookRequestProto.getDuration
      , getRoomsInfo(preBookRequestProto.getRoomsList)
      , preBookRequestProto.getCurrencyID
      , preBookRequestProto.getSuccess
      , preBookRequestProto.getErrorMessage
      , preBookRequestProto.getErrorStackTrace)

  }

  def messageSupplierPreBookRequestHandler(delivery: Delivery): SupplierPreBookRequest = {

    val supplierPreBookRequestProto = com.jactravel.monitoring.SupplierPreBookRequest.PARSER.parseFrom(delivery.getBody)

    SupplierPreBookRequest(
      supplierPreBookRequestProto.getQueryUUID
      , supplierPreBookRequestProto.getHost
      , supplierPreBookRequestProto.getSource
      , supplierPreBookRequestProto.getStartUtcTimestamp
      , supplierPreBookRequestProto.getEndUtcTimestamp
      , supplierPreBookRequestProto.getTimeout
      , supplierPreBookRequestProto.getPropertyCount
      , supplierPreBookRequestProto.getSuccess
      , supplierPreBookRequestProto.getErrorMessage
      , supplierPreBookRequestProto.getErrorStackTrace
      , supplierPreBookRequestProto.getRequestXML
      , supplierPreBookRequestProto.getResponseXML
      , supplierPreBookRequestProto.getPropertyCount)

  }

  def messageQueryProxyHandler(delivery: Delivery): QueryProxyRequest = {

    val queryProxyRequest = com.jactravel.monitoring.QueryProxyRequest.PARSER.parseFrom(delivery.getBody)

    QueryProxyRequest(
      queryProxyRequest.getQueryUUID
      , queryProxyRequest.getClientIP
      , queryProxyRequest.getSearchQueryType.getNumber
      , queryProxyRequest.getHost
      , DateTimeUtils.parseDate(queryProxyRequest.getClientRequestUtcTimestamp)
      , DateTimeUtils.parseDate(queryProxyRequest.getClientResponseUtcTimestamp)
      , DateTimeUtils.parseDate(queryProxyRequest.getForwardedRequestUtcTimestamp)
      , DateTimeUtils.parseDate(queryProxyRequest.getForwardedResponseUtcTimestamp)
      , queryProxyRequest.getRequestXML
      , queryProxyRequest.getResponseXML
      , queryProxyRequest.getXmlBookingLogin
      , queryProxyRequest.getSuccess
      , queryProxyRequest.getErrorMessage
      , queryProxyRequest.getRequestProcessor.getNumber
      , queryProxyRequest.getRequestURL
      , queryProxyRequest.getErrorStackTrace
    )

  }

  def messageSearchRequestHandler(delivery: Delivery): SearchRequest = {

    val searchRequestProto = com.jactravel.monitoring.SearchRequest.PARSER.parseFrom(delivery.getBody)
    val searchRequestInfo = searchRequestProto.getRequestInfo
    val searchResponseInfo = searchRequestProto.getResponseInfo

    val searchRequestInfoRes = RequestInfo(
      searchRequestInfo.getStartUtcTimestamp
      , searchRequestInfo.getEndUtcTimestamp
      , searchRequestInfo.getTradeID
      , searchRequestInfo.getBrandID
      , searchRequestInfo.getSalesChannelID
      , searchRequestInfo.getSearchGeoLevel.getNumber
      , searchRequestInfo.getGeoLevel1ID
      , searchRequestInfo.getGeoLevel2ID
      , searchRequestInfo.getGeoLevel3IDsList.asScala.map(_.asInstanceOf[Int]).toList
      , searchRequestInfo.getPropertyReferenceIDsList.asScala.map(_.asInstanceOf[Int]).toList
      , searchRequestInfo.getPropertyIDsList.asScala.map(_.asInstanceOf[Int]).toList
      , searchRequestInfo.getMinStarRating
      , searchRequestInfo.getArrivalDate
      , searchRequestInfo.getDuration
      , searchRequestInfo.getMealBasisID
      , searchRequestInfo.getRoomsList.asScala.map(rr => RoomRequest(
        rr.getAdults
        , rr.getChildren
        , rr.getChildAgesList.asScala.map(_.asInstanceOf[Int]).toList)).toList
      , searchRequestInfo.getRoomCount
    )

    val searchResponseInfoRes = ResponseInfo(
      searchResponseInfo.getPropertyReferenceCount
      , searchResponseInfo.getPropertyCount
      , searchResponseInfo.getPricedRoomCount
      , searchResponseInfo.getSuppliersSearchedList.asScala.toList
      , searchResponseInfo.getSuccess
      , searchResponseInfo.getErrorMessage
      , searchResponseInfo.getErrorStackTrace
    )

    SearchRequest(
      searchRequestProto.getQueryUUID
      , searchRequestProto.getHost
      , searchRequestInfoRes
      , searchResponseInfoRes
    )

  }

  def messageSupplierSearchRequestHandler(delivery: Delivery): SupplierSearchRequest = {

    val supplierSearchRequestProto = com.jactravel.monitoring.SupplierSearchRequest.PARSER.parseFrom(delivery.getBody)

    SupplierSearchRequest(
      supplierSearchRequestProto.getQueryUUID
      , supplierSearchRequestProto.getHost
      , supplierSearchRequestProto.getSource
      , supplierSearchRequestProto.getStartUtcTimestamp
      , supplierSearchRequestProto.getEndUtcTimestamp
      , supplierSearchRequestProto.getTimeout
      , supplierSearchRequestProto.getPropertyCount
      , supplierSearchRequestProto.getSuccess
      , supplierSearchRequestProto.getErrorMessage
      , supplierSearchRequestProto.getErrorStackTrace
      , supplierSearchRequestProto.getRequestXML
      , supplierSearchRequestProto.getResponseXML
      , supplierSearchRequestProto.getPropertyCount)

  }

  def messageCmiRequestHandler(delivery: Delivery): CmiRequest = {
    val cmiRequestProto = com.jactravel.monitoring.CMIRequest.PARSER.parseFrom(delivery.getBody)

    CmiRequest(
      cmiRequestProto.getQueryUUID
      , cmiRequestProto.getSupplierIP
      , cmiRequestProto.getCMIQueryType.getNumber
      , cmiRequestProto.getHost
      , cmiRequestProto.getClientRequestUtcTimestamp
      , cmiRequestProto.getClientResponseUtcTimestamp
      , cmiRequestProto.getForwardedRequestUtcTimestamp
      , cmiRequestProto.getForwardedResponseUtcTimestamp
      , cmiRequestProto.getRequestXML
      , cmiRequestProto.getResponseXML
      , cmiRequestProto.getXmlBookingLogin
      , cmiRequestProto.getSuccess
      , cmiRequestProto.getErrorMessage
      , cmiRequestProto.getRequestProcessor.getNumber
      , cmiRequestProto.getRequestURL
      , cmiRequestProto.getErrorStackTrace
    )
  }

  def messageCmiBatchRequestHandler(delivery: Delivery): CmiBatchRequest = {
    val cmiBatchRequestProto = com.jactravel.monitoring.CMIBatchRequest.PARSER.parseFrom(delivery.getBody)

    CmiBatchRequest(
      cmiBatchRequestProto.getQueryUUID
      , cmiBatchRequestProto.getSupplierIP
      , cmiBatchRequestProto.getCMIQueryType.getNumber
      , cmiBatchRequestProto.getHost
      , cmiBatchRequestProto.getRequestUtcTimestamp
      , cmiBatchRequestProto.getResponseUtcTimestamp
      , cmiBatchRequestProto.getRequestXML
      , cmiBatchRequestProto.getResponseXML
      , cmiBatchRequestProto.getSuccess
      , cmiBatchRequestProto.getErrorMessage
      , cmiBatchRequestProto.getErrorStackTrace
    )
  }

  private[streaming] def getRoomsInfo(roomsList: java.util.List[com.jactravel.monitoring.BookRoomInfo]) = {
    roomsList.asScala.map {
      roomInfo =>
        BookRoomInfo(
          roomInfo.getAdults
          , roomInfo.getChildren
          , roomInfo.getChildAgesList.asScala.toList.map(_.asInstanceOf[Int])
          , roomInfo.getMealBasisID
          , roomInfo.getBookingToken
          , roomInfo.getPropertyRoomTypeID
          , roomInfo.getPriceDiff
          , roomInfo.getRoomCount
          , roomInfo.getPreBookingToken
        )
    }.toList
  }
}
