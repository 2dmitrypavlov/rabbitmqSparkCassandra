// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: BookRequest.proto

package com.jactravel.monitoring;

public interface BookRequestOrBuilder
    extends com.google.protobuf.MessageOrBuilder {

  // optional string queryUUID = 1;
  /**
   * <code>optional string queryUUID = 1;</code>
   */
  boolean hasQueryUUID();
  /**
   * <code>optional string queryUUID = 1;</code>
   */
  java.lang.String getQueryUUID();
  /**
   * <code>optional string queryUUID = 1;</code>
   */
  com.google.protobuf.ByteString
      getQueryUUIDBytes();

  // optional string searchQueryUUID = 2;
  /**
   * <code>optional string searchQueryUUID = 2;</code>
   */
  boolean hasSearchQueryUUID();
  /**
   * <code>optional string searchQueryUUID = 2;</code>
   */
  java.lang.String getSearchQueryUUID();
  /**
   * <code>optional string searchQueryUUID = 2;</code>
   */
  com.google.protobuf.ByteString
      getSearchQueryUUIDBytes();

  // optional string preBookQueryUUID = 3;
  /**
   * <code>optional string preBookQueryUUID = 3;</code>
   */
  boolean hasPreBookQueryUUID();
  /**
   * <code>optional string preBookQueryUUID = 3;</code>
   */
  java.lang.String getPreBookQueryUUID();
  /**
   * <code>optional string preBookQueryUUID = 3;</code>
   */
  com.google.protobuf.ByteString
      getPreBookQueryUUIDBytes();

  // optional .Jactravel.Monitoring.PlatformType searchProcessor = 4;
  /**
   * <code>optional .Jactravel.Monitoring.PlatformType searchProcessor = 4;</code>
   */
  boolean hasSearchProcessor();
  /**
   * <code>optional .Jactravel.Monitoring.PlatformType searchProcessor = 4;</code>
   */
  com.jactravel.monitoring.PlatformType getSearchProcessor();

  // optional string host = 5;
  /**
   * <code>optional string host = 5;</code>
   */
  boolean hasHost();
  /**
   * <code>optional string host = 5;</code>
   */
  java.lang.String getHost();
  /**
   * <code>optional string host = 5;</code>
   */
  com.google.protobuf.ByteString
      getHostBytes();

  // optional string startUtcTimestamp = 6;
  /**
   * <code>optional string startUtcTimestamp = 6;</code>
   */
  boolean hasStartUtcTimestamp();
  /**
   * <code>optional string startUtcTimestamp = 6;</code>
   */
  java.lang.String getStartUtcTimestamp();
  /**
   * <code>optional string startUtcTimestamp = 6;</code>
   */
  com.google.protobuf.ByteString
      getStartUtcTimestampBytes();

  // optional string endUtcTimestamp = 7;
  /**
   * <code>optional string endUtcTimestamp = 7;</code>
   */
  boolean hasEndUtcTimestamp();
  /**
   * <code>optional string endUtcTimestamp = 7;</code>
   */
  java.lang.String getEndUtcTimestamp();
  /**
   * <code>optional string endUtcTimestamp = 7;</code>
   */
  com.google.protobuf.ByteString
      getEndUtcTimestampBytes();

  // optional int32 tradeID = 8;
  /**
   * <code>optional int32 tradeID = 8;</code>
   */
  boolean hasTradeID();
  /**
   * <code>optional int32 tradeID = 8;</code>
   */
  int getTradeID();

  // optional int32 brandID = 9;
  /**
   * <code>optional int32 brandID = 9;</code>
   */
  boolean hasBrandID();
  /**
   * <code>optional int32 brandID = 9;</code>
   */
  int getBrandID();

  // optional int32 salesChannelID = 10;
  /**
   * <code>optional int32 salesChannelID = 10;</code>
   */
  boolean hasSalesChannelID();
  /**
   * <code>optional int32 salesChannelID = 10;</code>
   */
  int getSalesChannelID();

  // optional int32 propertyID = 11;
  /**
   * <code>optional int32 propertyID = 11;</code>
   */
  boolean hasPropertyID();
  /**
   * <code>optional int32 propertyID = 11;</code>
   */
  int getPropertyID();

  // optional string arrivalDate = 12;
  /**
   * <code>optional string arrivalDate = 12;</code>
   */
  boolean hasArrivalDate();
  /**
   * <code>optional string arrivalDate = 12;</code>
   */
  java.lang.String getArrivalDate();
  /**
   * <code>optional string arrivalDate = 12;</code>
   */
  com.google.protobuf.ByteString
      getArrivalDateBytes();

  // optional int32 duration = 13;
  /**
   * <code>optional int32 duration = 13;</code>
   */
  boolean hasDuration();
  /**
   * <code>optional int32 duration = 13;</code>
   */
  int getDuration();

  // repeated .Jactravel.Monitoring.BookRoomInfo rooms = 14;
  /**
   * <code>repeated .Jactravel.Monitoring.BookRoomInfo rooms = 14;</code>
   */
  java.util.List<com.jactravel.monitoring.BookRoomInfo> 
      getRoomsList();
  /**
   * <code>repeated .Jactravel.Monitoring.BookRoomInfo rooms = 14;</code>
   */
  com.jactravel.monitoring.BookRoomInfo getRooms(int index);
  /**
   * <code>repeated .Jactravel.Monitoring.BookRoomInfo rooms = 14;</code>
   */
  int getRoomsCount();
  /**
   * <code>repeated .Jactravel.Monitoring.BookRoomInfo rooms = 14;</code>
   */
  java.util.List<? extends com.jactravel.monitoring.BookRoomInfoOrBuilder> 
      getRoomsOrBuilderList();
  /**
   * <code>repeated .Jactravel.Monitoring.BookRoomInfo rooms = 14;</code>
   */
  com.jactravel.monitoring.BookRoomInfoOrBuilder getRoomsOrBuilder(
      int index);

  // optional int32 currencyID = 15;
  /**
   * <code>optional int32 currencyID = 15;</code>
   */
  boolean hasCurrencyID();
  /**
   * <code>optional int32 currencyID = 15;</code>
   */
  int getCurrencyID();

  // optional string preBookingToken = 16;
  /**
   * <code>optional string preBookingToken = 16;</code>
   */
  boolean hasPreBookingToken();
  /**
   * <code>optional string preBookingToken = 16;</code>
   */
  java.lang.String getPreBookingToken();
  /**
   * <code>optional string preBookingToken = 16;</code>
   */
  com.google.protobuf.ByteString
      getPreBookingTokenBytes();
}
