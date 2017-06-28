CREATE KEYSPACE jactravel_monitoring
  WITH replication = {
  'class' : 'SimpleStrategy',
  'replication_factor' : 1
};

/*
message RoomRequest {
	optional int32 adults = 1;
	optional int32 children = 2;
	repeated int32 childAges = 3;
}
*/

CREATE TYPE jactravel_monitoring.room_request (
  adults int,
  children int,
  child_ages LIST<int>);

/*
message BookRoomInfo {
	optional int32 adults = 1;
	optional int32 children = 2;
	repeated int32 childAges = 3;
	optional int32 mealBasisID = 4;
	optional string bookingToken = 5;
	optional int32 propertyRoomTypeID = 6;
	optional string priceDiff = 7;
	optional int32 roomCount = 8;
	optional string preBookingToken = 9;
}
*/

CREATE TYPE jactravel_monitoring.book_room_info (
  adults int,
  children int,
  child_ages LIST<int>,
  meal_basis_id int,
  booking_token text,
  property_room_type_id int,
  price_diff text,
  room_count int,
  pre_booking_token text
);

/*
message BookRequest {
	optional string queryUUID = 1;
	optional string searchQueryUUID = 2;
	optional string preBookQueryUUID = 3;
	optional PlatformType searchProcessor = 4;
	optional string host = 5;
	optional string startUtcTimestamp = 6;
	optional string endUtcTimestamp = 7;
	optional int32 tradeID = 8;
	optional int32 brandID = 9;
	optional int32 salesChannelID = 10;
	optional int32 propertyID = 11;
	optional string arrivalDate = 12;
	optional int32 duration = 13;
	repeated BookRoomInfo rooms = 14;
	optional int32 currencyID = 15;
	optional string success =16;
	optional string errorMessage = 17;
	optional string errorStackTrace = 18;
}
*/

CREATE TABLE jactravel_monitoring.book_request (
  query_uuid text,
  search_query_uuid text,
  pre_book_query_uuid text,
  search_processor int,
  host text,
  start_utc_timestamp text,
  end_utc_timestamp text,
  trade_id int,
  brand_id int,
  sales_channel_id int,
  property_id int,
  arrival_date text,
  duration int,
  rooms FROZEN<LIST<book_room_info>>,
  currency_id int,
  success text,
  error_message text,
  error_stack_trace text,
  PRIMARY KEY (query_uuid)
);

/*
message PreBookRequest {
	optional string queryUUID = 1;
	optional string searchQueryUUID = 2;
	optional PlatformType searchProcessor = 3;
	optional string host = 4;
	optional string startUtcTimestamp = 5;
	optional string endUtcTimestamp = 6;
	optional int32 tradeID = 7;
	optional int32 brandID = 8;
	optional int32 salesChannelID = 9;
	optional int32 propertyID = 10;
	optional string arrivalDate = 11;
	optional int32 duration = 12;
	repeated BookRoomInfo rooms = 13;
	optional int32 currencyID = 14;
	optional string success = 15;
	optional string errorMessage = 16;
	optional string errorStackTrace = 17;
}
*/

CREATE TABLE jactravel_monitoring.pre_book_request (
  query_uuid text,
  search_query_uuid text,
  search_processor int,
  host text,
  start_utc_timestamp text,
  end_utc_timestamp text,
  trade_id int,
  brand_id int,
  sales_channel_id int,
  property_id int,
  arrival_date text,
  duration int,
  rooms FROZEN<LIST<book_room_info>>,
  currency_id int,
  success text,
  error_message text,
  error_stack_trace text,
  PRIMARY KEY (trade_id, query_uuid)
);

/*
message QueryProxyRequest {
	optional string queryUUID = 1;
	optional string clientIP = 2;
	optional QueryType searchQueryType = 3;
	optional string host = 5;
	optional string clientRequestUtcTimestamp = 6;
	optional string clientResponseUtcTimestamp = 7;
	optional string forwardedRequestUtcTimestamp = 8;
	optional string forwardedResponseUtcTimestamp = 9;
	optional string requestXML = 10;
	optional string responseXML = 11;
	optional string xmlBookingLogin = 12;
	optional string success = 13;
	optional string errorMessage = 14;
	optional PlatformType requestProcessor = 15;
	optional string requestURL = 16;
	optional string errorStackTrace = 17;
}
*/

CREATE TABLE jactravel_monitoring.query_proxy_request (
  query_uuid text,
  client_ip text,
  search_query_type int,
  host text,
  client_request_utc_timestamp text,
  client_response_utc_timestamp text,
  forwarded_request_utc_timestamp text,
  forwarded_response_utc_timestamp text,
  request_xml text,
  response_xml text,
  xml_booking_login text,
  success text,
  error_message text,
  request_processor int,
  request_url text,
  error_stack_trace text,
  PRIMARY KEY (query_uuid)
);

/*
message SearchRequestInfo {
	optional string startUtcTimestamp = 1;
	optional string endUtcTimestamp = 2;
	optional int32 tradeID = 3;
	optional int32 brandID = 4;
	optional int32 salesChannelID = 5;
	optional GeoLevel searchGeoLevel = 6;
	optional int32 geoLevel1ID = 7;
	optional int32 geoLevel2ID = 8;
	repeated int32 geoLevel3IDs = 9;
	repeated int32 propertyReferenceIDs = 10;
	repeated int32 propertyIDs = 11;
	optional string minStarRating = 12;
	optional string arrivalDate = 13;
	optional int32 duration = 14;
	optional int32 mealBasisID = 15;
	repeated RoomRequest rooms = 16;	// These are rooms specified in the search-request.
	optional int32 roomCount = 17;
}
*/

CREATE TYPE jactravel_monitoring.search_request_info (
  start_utc_timestamp text,
  end_utc_timestamp text,
  trade_id int,
  brand_id int,
  sales_channel_id int,
  search_geo_level int,
  geo_level1_id int,
  geo_level2_id int,
  geo_level3_ids LIST<int>,
  property_reference_ids LIST<int>,
  property_ids LIST<int>,
  min_star_rating text,
  arrival_date text,
  duration int,
  meal_basis_id int,
  rooms FROZEN<LIST<room_request>>,
  room_count int
);

/*
message SearchResponseInfo {
	optional int32 propertyReferenceCount = 1;	// total number of unique Properties retunred in a search (same as TotalProperties in search response xml)
	optional int32 propertyCount = 2;			// total number of unique Properties processed for the search. (i.e. Property Count before the deduplication process)
	optional int32 pricedRoomCount = 3;
	repeated string suppliersSearched = 4;
	optional string success = 5;
	optional string errorMessage = 6;
	optional string errorStackTrace = 7;
}
*/

CREATE TYPE jactravel_monitoring.search_response_info (
  property_reference_count int,
  property_count int,
  priced_room_count int,
  suppliers_searched list<text>,
  success text,
  error_message text,
  error_stack_trace text
);

/*
message SearchRequest {
	optional string queryUUID = 1;
	optional string host = 2;
	optional SearchRequestInfo requestInfo = 4;
	optional SearchResponseInfo responseInfo = 5;
}
*/

CREATE TABLE jactravel_monitoring.search_request (
  query_uuid text,
  host text,
  request_info FROZEN < search_request_info>,
  response_info FROZEN < search_response_info >,
  PRIMARY KEY (query_uuid)
);


/*
message SupplierBookRequest {
	optional string queryUUID = 1;
	optional string host = 2;
	optional string source = 3;
	optional string startUtcTimestamp = 4;
	optional string endUtcTimestamp = 5;
	optional int32 timeout = 6;
	optional int32 propertyCount = 7;
	optional string success = 8;
	optional string errorMessage = 9;
	optional string errorStackTrace = 10;
	optional string requestXML = 11;
	optional string responseXML = 12;
	optional int32 requestCount = 13;
}
*/

CREATE TABLE jactravel_monitoring.supplier_book_request (
  query_uuid text,
  host text,
  source text,
  start_utc_timestamp text,
  end_utc_timestamp text,
  timeout int,
  property_count int,
  success text,
  error_message text,
  error_stack_trace text,
  request_xml text,
  response_xml text,
  request_count int,
  PRIMARY KEY (query_uuid)
);

/*
message SupplierPreBookRequest {
	optional string queryUUID = 1;
	optional string host = 2;
	optional string source = 3;
	optional string startUtcTimestamp = 4;
	optional string endUtcTimestamp = 5;
	optional int32 timeout = 6;
	optional int32 propertyCount = 7;
	optional string success = 8;
	optional string errorMessage = 9;
	optional string errorStackTrace = 10;
	optional string requestXML = 11;
	optional string responseXML = 12;
	optional int32 requestCount = 13;
}
*/

CREATE TABLE jactravel_monitoring.supplier_pre_book_request (
  query_uuid text,
  host text,
  source text,
  start_utc_timestamp text,
  end_utc_timestamp text,
  timeout int,
  property_count int,
  success text,
  error_message text,
  error_stack_trace text,
  request_xml text,
  response_xml text,
  request_count int,
  PRIMARY KEY (query_uuid)
);

/*
message SupplierSearchRequest {
	optional string queryUUID = 1;
	optional string host = 2;
	optional string source = 3;
	optional string startUtcTimestamp = 4;
	optional string endUtcTimestamp = 5;
	optional int32 timeout = 6;
	optional int32 propertyCount = 7;
	optional string success = 8;
	optional string errorMessage = 9;
	optional string errorStackTrace = 10;
	optional string requestXML = 11;
	optional string responseXML = 12;
	optional int32 requestCount = 13;
}
*/

CREATE TABLE jactravel_monitoring.supplier_search_request (
  query_uuid text,
  host text,
  source text,
  start_utc_timestamp text,
  end_utc_timestamp text,
  timeout int,
  property_count int,
  success text,
  error_message text,
  error_stack_trace text,
  request_xml text,
  response_xml text,
  request_count int,
  PRIMARY KEY (query_uuid)
);

/*
message CMIRequest {
	optional string queryUUID = 1;
	optional string supplierIP = 2;
	optional CMIQueryType CMIQueryType = 3;
	optional string host = 5;
	optional string clientRequestUtcTimestamp = 6;
	optional string clientResponseUtcTimestamp = 7;
	optional string forwardedRequestUtcTimestamp = 8;
	optional string forwardedResponseUtcTimestamp = 9;
	optional string requestXML = 10;
	optional string responseXML = 11;
	optional string xmlBookingLogin = 12;
	optional string success = 13;
	optional string errorMessage = 14;
	optional PlatformType requestProcessor = 15;
	optional string requestURL = 16;
	optional string errorStackTrace = 17;
}
*/

CREATE TABLE jactravel_monitoring.cmi_request (
  query_uuid text,
  supplier_ip text,
  cmi_query_type int,
  host text,
  client_request_utc_timestamp text,
  client_response_utc_timestamp text,
  forwarded_request_utc_timestamp text,
  forwarded_response_utc_timestamp text,
  request_xml text,
  response_xml text,
  xml_booking_login text,
  success text,
  error_message text,
  request_processor int,
  request_url text,
  error_stack_trace text,
  PRIMARY KEY (query_uuid)
);

/*
message CMIBatchRequest {
	optional string queryUUID = 1;
	optional string supplierIP = 2;
	optional CMIQueryType CMIQueryType = 3;
	optional string host = 5;
	optional string requestUtcTimestamp = 6;
	optional string responseUtcTimestamp = 7;
	optional string requestXML = 10;
	optional string responseXML = 11;
	optional string success = 13;
	optional string errorMessage = 14;
	optional string errorStackTrace = 17;
}
*/

CREATE TABLE jactravel_monitoring.cmi_batch_request (
  query_uuid text,
  supplier_ip text,
  cmi_query_type int,
  host text,
  request_utc_timestamp text,
  response_utc_timestamp text,
  request_xml text,
  response_xml text,
  success text,
  error_message text,
  error_stack_trace text,
  PRIMARY KEY (query_uuid)
);

CREATE TABLE jactravel_monitoring.query_uuid_proceed (
  query_uuid text,
  proceed int,
  PRIMARY KEY (query_uuid)
);