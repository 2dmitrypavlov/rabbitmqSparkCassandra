-- BOOKING FAILURE
SELECT COUNT(query_uuid) as booking_errors,
	time AS tm,
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