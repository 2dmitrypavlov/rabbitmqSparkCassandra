// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: SearchRequest.proto

package com.jactravel.monitoring;

public interface SearchRequestOrBuilder
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

  // optional string host = 2;
  /**
   * <code>optional string host = 2;</code>
   */
  boolean hasHost();
  /**
   * <code>optional string host = 2;</code>
   */
  java.lang.String getHost();
  /**
   * <code>optional string host = 2;</code>
   */
  com.google.protobuf.ByteString
      getHostBytes();

  // optional .Jactravel.Monitoring.SearchRequestInfo requestInfo = 4;
  /**
   * <code>optional .Jactravel.Monitoring.SearchRequestInfo requestInfo = 4;</code>
   */
  boolean hasRequestInfo();
  /**
   * <code>optional .Jactravel.Monitoring.SearchRequestInfo requestInfo = 4;</code>
   */
  com.jactravel.monitoring.SearchRequestInfo getRequestInfo();
  /**
   * <code>optional .Jactravel.Monitoring.SearchRequestInfo requestInfo = 4;</code>
   */
  com.jactravel.monitoring.SearchRequestInfoOrBuilder getRequestInfoOrBuilder();

  // optional .Jactravel.Monitoring.SearchResponseInfo responseInfo = 5;
  /**
   * <code>optional .Jactravel.Monitoring.SearchResponseInfo responseInfo = 5;</code>
   */
  boolean hasResponseInfo();
  /**
   * <code>optional .Jactravel.Monitoring.SearchResponseInfo responseInfo = 5;</code>
   */
  com.jactravel.monitoring.SearchResponseInfo getResponseInfo();
  /**
   * <code>optional .Jactravel.Monitoring.SearchResponseInfo responseInfo = 5;</code>
   */
  com.jactravel.monitoring.SearchResponseInfoOrBuilder getResponseInfoOrBuilder();
}
