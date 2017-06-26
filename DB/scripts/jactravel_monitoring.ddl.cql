CREATE KEYSPACE jactravel_monitoring
  WITH replication = {
  'class' : 'SimpleStrategy',
  'replication_factor' : 1
};

CREATE TYPE jactravel_monitoring.room_request (
  adults int,
  children int,
  child_ages LIST<int>);

CREATE TYPE jactravel_monitoring.book_room_info (
  adults int,
  children int,
  child_ages LIST<int>,
  meal_basis_id int,
  booking_token text,
  property_room_type_id int,
  price_diff text
);

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
  pre_booking_token text,
  PRIMARY KEY ((trade_id, query_uuid), search_query_uuid)
);

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
  pre_booking_token text,
  PRIMARY KEY (trade_id, query_uuid)
);

CREATE TABLE jactravel_monitoring.query_proxy_request (
  query_uuid text,
  request_utc_timestamp timestamp,
  client_ip text,
  search_query_type int,
  host text,
  client_request_utc_timestamp timestamp,
  client_response_utc_timestamp timestamp,
  forwarded_request_utc_timestamp timestamp,
  forwarded_response_utc_timestamp timestamp,
  request_xml text,
  response_xml text,
  xml_booking_login text,
  success text,
  error_message text,
  request_processor int,
  request_url text,
  error_stack_trace text,
  PRIMARY KEY ((query_uuid, request_utc_timestamp), client_request_utc_timestamp, client_response_utc_timestamp)
);

CREATE TABLE jactravel_monitoring.search_request_info (
  query_uuid text,
  host text,
  start_utc_timestamp text,
  end_utc_timestamp text,
  trade_id int,
  brand_id int,
  sales_channel_id int,
  search_geo_level int,
  geo_level1_id int,
  geo_level2_id int,
  geo_level3_id LIST<int>,
  property_reference_ids LIST<int>,
  property_ids LIST<int>,
  min_star_rating text,
  arrival_date text,
  duration int,
  meal_basis_id int,
  rooms FROZEN<LIST<room_request>>,
  PRIMARY KEY (query_uuid, trade_id)
);

CREATE TABLE jactravel_monitoring.search_response_info (
  query_uuid text,
  host text,
  property_reference_count int,
  property_count int,
  priced_room_count int,
  success text,
  error_message text,
  error_stack_trace text,
  suppliers_searched list<text>,
  PRIMARY KEY (query_uuid)
);

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
  PRIMARY KEY (query_uuid)
);

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
  PRIMARY KEY (query_uuid)
);

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
  PRIMARY KEY (query_uuid)
);