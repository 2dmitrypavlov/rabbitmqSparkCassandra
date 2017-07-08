-- RICH BOOKING
SELECT br.query_uuid AS query_uuid,
	brand_name,
	trade_name,
	trade_group,
	trade_parent_group,
	sales_channel,
	(unix_timestamp(end_utc_timestamp) - unix_timestamp(start_utc_timestamp)) * 1000 AS response_time_ms,
	error_stack_trace,
	success,
	xml_booking_login,
	window(start_utc_timestamp, '5 minutes').end AS time
FROM BookRequest AS br,
	SalesChannel AS sc,
	Trade AS t,
	Brand AS b
	LEFT JOIN QueryProxyRequest AS qpr
		ON br.query_uuid == qpr.query_uuid
WHERE br.sales_channel_id == sc.sales_channel_id
AND br.trade_id == t.trade_id
AND br.brand_id == b.brand_id


-- BOOKING COUNT
SELECT COUNT(query_uuid) AS booking_count,
	time,
	brand_name,
	sales_channel,
	trade_group,
	trade_name,
	trade_parent_group,
	xmL_booking_login
FROM RichBooking
GROUP BY
	time,
	brand_name,
	sales_channel,
	trade_group,
	trade_name,
	trade_parent_group,
	xml_booking_login

-- BOOKING SUCCESS
SELECT COUNT(query_uuid) AS booking_success,
	time,
	brand_name,
	sales_channel,
	trade_group,
	trade_name,
	trade_parent_group,
	xmL_booking_login
FROM RichBooking
WHERE success IS NOT NULL
GROUP BY
	time,
	brand_name,
	sales_channel,
	trade_group,
	trade_name,
	trade_parent_group,
	xml_booking_login

-- BOOKING FAILURE
SELECT COUNT(query_uuid) as booking_errors,
	time,
	brand_name,
	sales_channel,
	trade_group,
	trade_name,
	trade_parent_group,
	xmL_booking_login
FROM RichBooking
WHERE error_stack_trace IS NOT NULL
GROUP BY
	time,
	brand_name,
	sales_channel,
	trade_group,
	trade_name,
	trade_parent_group,
	xml_booking_login



-- BOOKING RESPONSE TIME
SELECT COUNT(query_uuid) AS booking_errors,
	time,
	brand_name,
	sales_channel,
	trade_group,
	trade_name,
	trade_parent_group,
	xmL_booking_login,
	min(response_time_ms) AS min_response_time_ms,
  max(response_time_ms) AS max_response_time_ms,
	avg(response_time_ms) AS avg_response_time_ms
FROM RichBooking
GROUP BY
	time,
	brand_name,
	sales_channel,
	trade_group,
	trade_name,
	trade_parent_group,
	xml_booking_login