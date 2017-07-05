-- BOOKING COUNT
SELECT COUNT(query_uuid) AS booking_count,
	time AS tm,
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