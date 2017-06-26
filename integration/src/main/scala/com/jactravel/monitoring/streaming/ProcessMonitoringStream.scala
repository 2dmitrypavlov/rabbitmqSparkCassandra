package com.jactravel.monitoring.streaming

import java.time._
import java.time.format.DateTimeFormatter
import java.time.temporal.{ChronoUnit, TemporalUnit}
import java.util.Date

import com.jactravel.monitoring.model._
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
      , bookRequestProto.getPreBookingToken
      , bookRequestProto.getErrorMessage
      , bookRequestProto.getErrorStackTrace)

  }

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
      , supplierBookRequestProto.getErrorStackTrace)

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
      , preBookRequestProto.getPreBookingToken
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
      , supplierPreBookRequestProto.getErrorStackTrace)

  }

  def messageQueryProxyHandler(delivery: Delivery): QueryProxyRequest = {

    val queryProxyRequest = com.jactravel.monitoring.QueryProxyRequest.PARSER.parseFrom(delivery.getBody)

    QueryProxyRequest(
      queryProxyRequest.getQueryUUID
      , toDate(parseDateTime(queryProxyRequest.getClientRequestUtcTimestamp, ChronoUnit.MINUTES))
      , queryProxyRequest.getClientIP
      , queryProxyRequest.getSearchQueryType.getNumber
      , queryProxyRequest.getHost
      , parseDate(queryProxyRequest.getClientRequestUtcTimestamp)
      , parseDate(queryProxyRequest.getClientResponseUtcTimestamp)
      , parseDate(queryProxyRequest.getForwardedRequestUtcTimestamp)
      , parseDate(queryProxyRequest.getForwardedResponseUtcTimestamp)
      , queryProxyRequest.getRequestXML
      , queryProxyRequest.getResponseXML
      , queryProxyRequest.getXmlBookingLogin
      , queryProxyRequest.getSuccess
      , queryProxyRequest.getErrorMessage
      , queryProxyRequest.getRequestProcessor.getNumber
      , queryProxyRequest.getRequestURL
      , queryProxyRequest.getErrorStackTrace
      , System.currentTimeMillis()
    )

  }

  def messageSearchReportHandler(delivery: Delivery): (SearchRequestInfo, SearchResponseInfo) = {

    val searchReport = com.jactravel.monitoring.SearchReport.PARSER.parseFrom(delivery.getBody)
    val searchRequestInfo = searchReport.getRequestInfo
    val searchResponseInfo = searchReport.getResponseInfo

    val searchRequestInfoRes = SearchRequestInfo(
      searchReport.getQueryUUID
      , searchReport.getHost
      , searchRequestInfo.getStartUtcTimestamp
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
    )

    val searchResponseInfoRes = SearchResponseInfo(
      searchReport.getQueryUUID
      , searchReport.getHost
      , searchResponseInfo.getPropertyReferenceCount
      , searchResponseInfo.getPropertyCount
      , searchResponseInfo.getPricedRoomCount
      , searchResponseInfo.getSuccess
      , searchResponseInfo.getErrorMessage
      , searchResponseInfo.getErrorStackTrace
      , searchResponseInfo.getSuppliersSearchedList.asScala.toList
    )

    (searchRequestInfoRes, searchResponseInfoRes)

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
      , supplierSearchRequestProto.getErrorStackTrace)

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
        )
    }.toList
  }

  private[streaming] def parseDateTime(dateTime: String, temporalUnit: TemporalUnit = ChronoUnit.SECONDS) = {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS]")

    LocalDateTime.parse(dateTime, formatter).truncatedTo(temporalUnit)
  }

  private[streaming] def toDate(dateTime: LocalDateTime) = {
    Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant)
  }

  private[streaming] def parseDate(date: String) = {
    toDate(parseDateTime(date))
  }

}
