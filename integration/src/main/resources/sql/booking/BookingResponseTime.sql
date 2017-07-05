-- BOOKING RESPONSE TIME
SELECT time AS tm,
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
