// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: QueryProxyRequest.proto

package com.jactravel.monitoring;

public interface QueryProxyRequestOrBuilder
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

  // optional string clientIP = 2;
  /**
   * <code>optional string clientIP = 2;</code>
   */
  boolean hasClientIP();
  /**
   * <code>optional string clientIP = 2;</code>
   */
  java.lang.String getClientIP();
  /**
   * <code>optional string clientIP = 2;</code>
   */
  com.google.protobuf.ByteString
      getClientIPBytes();

  // optional .Jactravel.Monitoring.QueryType searchQueryType = 3;
  /**
   * <code>optional .Jactravel.Monitoring.QueryType searchQueryType = 3;</code>
   */
  boolean hasSearchQueryType();
  /**
   * <code>optional .Jactravel.Monitoring.QueryType searchQueryType = 3;</code>
   */
  com.jactravel.monitoring.QueryType getSearchQueryType();

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

  // optional string clientRequestUtcTimestamp = 6;
  /**
   * <code>optional string clientRequestUtcTimestamp = 6;</code>
   */
  boolean hasClientRequestUtcTimestamp();
  /**
   * <code>optional string clientRequestUtcTimestamp = 6;</code>
   */
  java.lang.String getClientRequestUtcTimestamp();
  /**
   * <code>optional string clientRequestUtcTimestamp = 6;</code>
   */
  com.google.protobuf.ByteString
      getClientRequestUtcTimestampBytes();

  // optional string clientResponseUtcTimestamp = 7;
  /**
   * <code>optional string clientResponseUtcTimestamp = 7;</code>
   */
  boolean hasClientResponseUtcTimestamp();
  /**
   * <code>optional string clientResponseUtcTimestamp = 7;</code>
   */
  java.lang.String getClientResponseUtcTimestamp();
  /**
   * <code>optional string clientResponseUtcTimestamp = 7;</code>
   */
  com.google.protobuf.ByteString
      getClientResponseUtcTimestampBytes();

  // optional string forwardedRequestUtcTimestamp = 8;
  /**
   * <code>optional string forwardedRequestUtcTimestamp = 8;</code>
   */
  boolean hasForwardedRequestUtcTimestamp();
  /**
   * <code>optional string forwardedRequestUtcTimestamp = 8;</code>
   */
  java.lang.String getForwardedRequestUtcTimestamp();
  /**
   * <code>optional string forwardedRequestUtcTimestamp = 8;</code>
   */
  com.google.protobuf.ByteString
      getForwardedRequestUtcTimestampBytes();

  // optional string forwardedResponseUtcTimestamp = 9;
  /**
   * <code>optional string forwardedResponseUtcTimestamp = 9;</code>
   */
  boolean hasForwardedResponseUtcTimestamp();
  /**
   * <code>optional string forwardedResponseUtcTimestamp = 9;</code>
   */
  java.lang.String getForwardedResponseUtcTimestamp();
  /**
   * <code>optional string forwardedResponseUtcTimestamp = 9;</code>
   */
  com.google.protobuf.ByteString
      getForwardedResponseUtcTimestampBytes();

  // optional string requestXML = 10;
  /**
   * <code>optional string requestXML = 10;</code>
   */
  boolean hasRequestXML();
  /**
   * <code>optional string requestXML = 10;</code>
   */
  java.lang.String getRequestXML();
  /**
   * <code>optional string requestXML = 10;</code>
   */
  com.google.protobuf.ByteString
      getRequestXMLBytes();

  // optional string responseXML = 11;
  /**
   * <code>optional string responseXML = 11;</code>
   */
  boolean hasResponseXML();
  /**
   * <code>optional string responseXML = 11;</code>
   */
  java.lang.String getResponseXML();
  /**
   * <code>optional string responseXML = 11;</code>
   */
  com.google.protobuf.ByteString
      getResponseXMLBytes();

  // optional string xmlBookingLogin = 12;
  /**
   * <code>optional string xmlBookingLogin = 12;</code>
   */
  boolean hasXmlBookingLogin();
  /**
   * <code>optional string xmlBookingLogin = 12;</code>
   */
  java.lang.String getXmlBookingLogin();
  /**
   * <code>optional string xmlBookingLogin = 12;</code>
   */
  com.google.protobuf.ByteString
      getXmlBookingLoginBytes();

  // optional string success = 13;
  /**
   * <code>optional string success = 13;</code>
   */
  boolean hasSuccess();
  /**
   * <code>optional string success = 13;</code>
   */
  java.lang.String getSuccess();
  /**
   * <code>optional string success = 13;</code>
   */
  com.google.protobuf.ByteString
      getSuccessBytes();

  // optional string errorMessage = 14;
  /**
   * <code>optional string errorMessage = 14;</code>
   */
  boolean hasErrorMessage();
  /**
   * <code>optional string errorMessage = 14;</code>
   */
  java.lang.String getErrorMessage();
  /**
   * <code>optional string errorMessage = 14;</code>
   */
  com.google.protobuf.ByteString
      getErrorMessageBytes();

  // optional .Jactravel.Monitoring.PlatformType requestProcessor = 15;
  /**
   * <code>optional .Jactravel.Monitoring.PlatformType requestProcessor = 15;</code>
   */
  boolean hasRequestProcessor();
  /**
   * <code>optional .Jactravel.Monitoring.PlatformType requestProcessor = 15;</code>
   */
  com.jactravel.monitoring.PlatformType getRequestProcessor();

  // optional string requestURL = 16;
  /**
   * <code>optional string requestURL = 16;</code>
   */
  boolean hasRequestURL();
  /**
   * <code>optional string requestURL = 16;</code>
   */
  java.lang.String getRequestURL();
  /**
   * <code>optional string requestURL = 16;</code>
   */
  com.google.protobuf.ByteString
      getRequestURLBytes();

  // optional string errorStackTrace = 17;
  /**
   * <code>optional string errorStackTrace = 17;</code>
   */
  boolean hasErrorStackTrace();
  /**
   * <code>optional string errorStackTrace = 17;</code>
   */
  java.lang.String getErrorStackTrace();
  /**
   * <code>optional string errorStackTrace = 17;</code>
   */
  com.google.protobuf.ByteString
      getErrorStackTraceBytes();
}
