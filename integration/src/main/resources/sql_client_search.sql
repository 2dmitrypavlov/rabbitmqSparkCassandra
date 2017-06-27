search_request_info AS SReq					// SearchRequest.proto
search_response_info AS SRes	
trade_catalog AS TC

val lowerBound = DateTime.now.minusMinutes(5)
val upperBound = DatetIME.now

SReq.createOrReplaceTempView("search_request_table")
SRes.createOrReplaceTempView("search_response_table)
TC.createOrReplaceTempView("trade_table")

val searchRequest = spark.sql("
SELECT query_uuid, brand_id, start_utc_timestamp, end_utc_timestamp, trade_id, error_stack_trace, success
FROM search_request_table
JOIN search_response_table
ON search_request_table.query_uuid == search_response_table.query_uuid
WHERE search_request_table.start_utc_timestamp BETWEEN lowerBound AND upperBound")

searchRequest.createOrReplaceTempView("search_request")

val influxResponseTime = spark.sql("
SELECT	MIN(DATEDIFF(end_utc_timestamp - start_utc_timestamp)) AS 'min_response_time',
	MAX(DATEDIFF(end_utc_timestamp - start_utc_timestamp)) AS 'max_response_time,
	ANG(DATEDIFF(end_utc_timestamp, start_utc_timestamp)) AS 'avg_response_time
FROM search_request 
JOIN trade_table
ON search_request.trade_id == trade_table.trade_id
GROUP BY start_utc_timestamp, brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name")



val influxRequestFailure = spark.sql("
SELECT	COUNT(query_uuid) AS 'failure_count'
FROM search_request 
JOIN trade_table
ON search_request.trade_id == trade_table.trade_id
WHERE search_request.error_stack_trace > 0 
AND search_request.success == 'false'
GROUP BY start_utc_timestamp, brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name")


//time is rounded to 5 min, so every 5 minute we create new data
val influxSearchCount = spark.sql("
SELECT	'11:35' as 'timestamp' ,COUNT(query_uuid) AS 'search_count', brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, XMLBookingLogin
FROM search_request
JOIN trade_table
ON search_request.trade_id == trade_table.trade_id
GROUP BY  brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, XMLBookingLogin")