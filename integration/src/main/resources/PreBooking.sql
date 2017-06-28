-- Pre-Booking
SELECT * FROM PreBookRequest AS PBR, SalesChannel AS SC, Bramd AS B, Trade AS T
LEFT JOIN QueryProxyRequest AS Q
ON PBR.query_uuid == Q.query_uuid
WHERE PBR.trade_id == T.trade_id
AND PBR.brand_id == B.brand_id
AND PBR.sales_channel_id == S.sales_channel_id


-- Pre-booking count
SELECT COUNT(query_uuid) AS 'pre_booking_count', brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login
FROM PreBooking
GROUP BY
brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_logi
ALTER TABLE 'pre_booking_count'
ADD 'time' DATE NOT NULL DEFAULT('2017-06-27 11:35:00')

-- Pre-booking success
SELECT COUNT(query_uuid) AS 'pre_booking_success', brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login
FROM PreBooking
WHERE success IS NOT NULL
GROUP BY
brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login
ALTER TABLE 'pre_booking_success'
ADD 'time' DATE NOT NULL DEFAULT('2017-06-27 11:35:00')

-- Pre-booking error
SELECT COUNT(query_uuid) AS 'pre_booking_error', brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login
FROM PreBooking
WHERE errorStackTrace IS NOT NULL
GROUP BY
brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login
ALTER TABLE 'pre_booking_error'
ADD 'time' DATE NOT NULL DEFAULT('2017-06-27 11:35:00')

-- Pre-booking response time
SELECT	brand_name,
	sales_channel_name,
	trade_group,
	trade_name,
	trade_parent_name,
	MIN(DATEDIFF(end_utc_timestamp - start_utc_timestamp)) AS 'min_response_time',
	MAX(DATEDIFF(end_utc_timestamp - start_utc_timestamp)) AS 'max_response_time',
	AVG(DATEDIFF(end_utc_timestamp, start_utc_timestamp)) AS 'avg_response_time'
FROM PreBooking
GROUP BY brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login
ALTER TABLE 'pre_booking_response_time'
ADD 'time' DATE NOT NULL DEFAULT('2017-06-27 11:35:00')
