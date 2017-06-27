//Booking
select *
from BookRequest as br, SalesChannel as s, Brand as b,Trade as t
LEFT JOIN QueryProxyRequest q
on br.query_uuid==q.query_uuid
where br.trade_id == t.trade_id and br.brand_id==b.brand_id and br.sales_channel_id=s.sales_channel_id


//count
select '11:35' as 'timestamp', COUNT(query_uuid) AS 'booking_count', brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login
from Booking
group by
brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login

//success
select '11:35' as 'timestamp', COUNT(query_uuid) AS 'booking_success', brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login
from Booking
where success is not null
group by
brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login

//error
select '11:35' as 'timestamp', COUNT(query_uuid) AS 'booking_error', brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login
from Booking
where errorStackTrace is not null
group by
brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login



// BOOKING RESPONSE TIME
val influxPreBookingResponseTime = spark.sql("
SELECT	brand_name,
	sales_channel_name,
	trade_group,
	trade_name,
	trade_parent_name,
	MIN(DATEDIFF(end_utc_timestamp - start_utc_timestamp)) AS 'min_response_time',
	MAX(DATEDIFF(end_utc_timestamp - start_utc_timestamp)) AS 'max_response_time,
	ANG(DATEDIFF(end_utc_timestamp, start_utc_timestamp)) AS 'avg_response_time
FROM Booking
GROUP BY brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login")
.withColumn("time", time)
