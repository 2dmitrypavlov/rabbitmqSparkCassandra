// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: SearchRequest.proto

package com.jactravel.monitoring;

public interface SearchRequestInfoOrBuilder
    extends com.google.protobuf.MessageOrBuilder {

  // optional string startUtcTimestamp = 1;
  /**
   * <code>optional string startUtcTimestamp = 1;</code>
   */
  boolean hasStartUtcTimestamp();
  /**
   * <code>optional string startUtcTimestamp = 1;</code>
   */
  java.lang.String getStartUtcTimestamp();
  /**
   * <code>optional string startUtcTimestamp = 1;</code>
   */
  com.google.protobuf.ByteString
      getStartUtcTimestampBytes();

  // optional string endUtcTimestamp = 2;
  /**
   * <code>optional string endUtcTimestamp = 2;</code>
   */
  boolean hasEndUtcTimestamp();
  /**
   * <code>optional string endUtcTimestamp = 2;</code>
   */
  java.lang.String getEndUtcTimestamp();
  /**
   * <code>optional string endUtcTimestamp = 2;</code>
   */
  com.google.protobuf.ByteString
      getEndUtcTimestampBytes();

  // optional int32 tradeID = 3;
  /**
   * <code>optional int32 tradeID = 3;</code>
   */
  boolean hasTradeID();
  /**
   * <code>optional int32 tradeID = 3;</code>
   */
  int getTradeID();

  // optional int32 brandID = 4;
  /**
   * <code>optional int32 brandID = 4;</code>
   */
  boolean hasBrandID();
  /**
   * <code>optional int32 brandID = 4;</code>
   */
  int getBrandID();

  // optional int32 salesChannelID = 5;
  /**
   * <code>optional int32 salesChannelID = 5;</code>
   */
  boolean hasSalesChannelID();
  /**
   * <code>optional int32 salesChannelID = 5;</code>
   */
  int getSalesChannelID();

  // optional .Jactravel.Monitoring.GeoLevel searchGeoLevel = 6;
  /**
   * <code>optional .Jactravel.Monitoring.GeoLevel searchGeoLevel = 6;</code>
   */
  boolean hasSearchGeoLevel();
  /**
   * <code>optional .Jactravel.Monitoring.GeoLevel searchGeoLevel = 6;</code>
   */
  com.jactravel.monitoring.GeoLevel getSearchGeoLevel();

  // optional int32 geoLevel1ID = 7;
  /**
   * <code>optional int32 geoLevel1ID = 7;</code>
   */
  boolean hasGeoLevel1ID();
  /**
   * <code>optional int32 geoLevel1ID = 7;</code>
   */
  int getGeoLevel1ID();

  // optional int32 geoLevel2ID = 8;
  /**
   * <code>optional int32 geoLevel2ID = 8;</code>
   */
  boolean hasGeoLevel2ID();
  /**
   * <code>optional int32 geoLevel2ID = 8;</code>
   */
  int getGeoLevel2ID();

  // repeated int32 geoLevel3IDs = 9;
  /**
   * <code>repeated int32 geoLevel3IDs = 9;</code>
   */
  java.util.List<java.lang.Integer> getGeoLevel3IDsList();
  /**
   * <code>repeated int32 geoLevel3IDs = 9;</code>
   */
  int getGeoLevel3IDsCount();
  /**
   * <code>repeated int32 geoLevel3IDs = 9;</code>
   */
  int getGeoLevel3IDs(int index);

  // repeated int32 propertyReferenceIDs = 10;
  /**
   * <code>repeated int32 propertyReferenceIDs = 10;</code>
   */
  java.util.List<java.lang.Integer> getPropertyReferenceIDsList();
  /**
   * <code>repeated int32 propertyReferenceIDs = 10;</code>
   */
  int getPropertyReferenceIDsCount();
  /**
   * <code>repeated int32 propertyReferenceIDs = 10;</code>
   */
  int getPropertyReferenceIDs(int index);

  // repeated int32 propertyIDs = 11;
  /**
   * <code>repeated int32 propertyIDs = 11;</code>
   */
  java.util.List<java.lang.Integer> getPropertyIDsList();
  /**
   * <code>repeated int32 propertyIDs = 11;</code>
   */
  int getPropertyIDsCount();
  /**
   * <code>repeated int32 propertyIDs = 11;</code>
   */
  int getPropertyIDs(int index);

  // optional string minStarRating = 12;
  /**
   * <code>optional string minStarRating = 12;</code>
   */
  boolean hasMinStarRating();
  /**
   * <code>optional string minStarRating = 12;</code>
   */
  java.lang.String getMinStarRating();
  /**
   * <code>optional string minStarRating = 12;</code>
   */
  com.google.protobuf.ByteString
      getMinStarRatingBytes();

  // optional string arrivalDate = 13;
  /**
   * <code>optional string arrivalDate = 13;</code>
   */
  boolean hasArrivalDate();
  /**
   * <code>optional string arrivalDate = 13;</code>
   */
  java.lang.String getArrivalDate();
  /**
   * <code>optional string arrivalDate = 13;</code>
   */
  com.google.protobuf.ByteString
      getArrivalDateBytes();

  // optional int32 duration = 14;
  /**
   * <code>optional int32 duration = 14;</code>
   */
  boolean hasDuration();
  /**
   * <code>optional int32 duration = 14;</code>
   */
  int getDuration();

  // optional int32 mealBasisID = 15;
  /**
   * <code>optional int32 mealBasisID = 15;</code>
   */
  boolean hasMealBasisID();
  /**
   * <code>optional int32 mealBasisID = 15;</code>
   */
  int getMealBasisID();

  // repeated .Jactravel.Monitoring.RoomRequest rooms = 16;
  /**
   * <code>repeated .Jactravel.Monitoring.RoomRequest rooms = 16;</code>
   *
   * <pre>
   * These are rooms specified in the search-request.
   * </pre>
   */
  java.util.List<com.jactravel.monitoring.RoomRequest> 
      getRoomsList();
  /**
   * <code>repeated .Jactravel.Monitoring.RoomRequest rooms = 16;</code>
   *
   * <pre>
   * These are rooms specified in the search-request.
   * </pre>
   */
  com.jactravel.monitoring.RoomRequest getRooms(int index);
  /**
   * <code>repeated .Jactravel.Monitoring.RoomRequest rooms = 16;</code>
   *
   * <pre>
   * These are rooms specified in the search-request.
   * </pre>
   */
  int getRoomsCount();
  /**
   * <code>repeated .Jactravel.Monitoring.RoomRequest rooms = 16;</code>
   *
   * <pre>
   * These are rooms specified in the search-request.
   * </pre>
   */
  java.util.List<? extends com.jactravel.monitoring.RoomRequestOrBuilder> 
      getRoomsOrBuilderList();
  /**
   * <code>repeated .Jactravel.Monitoring.RoomRequest rooms = 16;</code>
   *
   * <pre>
   * These are rooms specified in the search-request.
   * </pre>
   */
  com.jactravel.monitoring.RoomRequestOrBuilder getRoomsOrBuilder(
      int index);

  // optional int32 roomCount = 17;
  /**
   * <code>optional int32 roomCount = 17;</code>
   */
  boolean hasRoomCount();
  /**
   * <code>optional int32 roomCount = 17;</code>
   */
  int getRoomCount();
}
