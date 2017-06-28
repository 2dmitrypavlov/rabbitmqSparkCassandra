//Booking
select *
from BookRequest as br, SalesChannel as s, Brand as b,Trade as t
LEFT JOIN QueryProxyRequest q
on br.query_uuid==q.query_uuid
where br.trade_id == t.trade_id and br.brand_id==b.brand_id and br.sales_channel_id=s.sales_channel_id

select *
from BookRequest as br, SalesChannel as s, Brand as b,Trade as t
LEFT JOIN QueryProxyRequest q
on br.query_uuid==q.query_uuid
LEFT JOIN br
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



[6/28/17, 9:19:00 AM] Alessandro Martino: On 6/27/17, at 4:29 PM, spark pavlov wrote:
> when we save to one table we should join
BookRequest left join BookRoomInfo
Left join QueryProxyRequest
when referring to Grafana all "transactions" are independent so no need to mix book with search. we will do that in zeppelin.
[6/28/17, 9:21:21 AM] Alessandro Martino: On 6/27/17, at 4:35 PM, spark pavlov wrote:
> select *
from BookRequest br, SalesChannel s, Brand b,Trade t,QueryProxyRequest qp
where br.trade_id == t.trade_id and br.brand_id==b.brand_id and br.sales_channel_id=s.sales_channel_id
LEFT JOIN QueryProxyRequest
on BookRequest.query_uuid==QueryProxyRequest.query_uuid
as Booking
your logic looks ok
[6/28/17, 9:23:24 AM] Alessandro Martino: On 6/27/17, at 4:35 PM, spark pavlov wrote:
> select *
from BookRequest br, SalesChannel s, Brand b,Trade t,QueryProxyRequest qp
where br.trade_id == t.trade_id and br.brand_id==b.brand_id and br.sales_channel_id=s.sales_channel_id
LEFT JOIN QueryProxyRequest
on BookRequest.query_uuid==QueryProxyRequest.query_uuid
as Booking

select '11:35' as 'timestamp', COUNT(query_uuid) AS 'search_count', brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login
from Booking
group by
brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login
Booking is stored in Cassandra, does it make sense?
The other select is stored in influx
again, queries look logically correct but I'm now a bit confused on where you are running them on and ot do what

[6/28/17, 9:38:55 AM] Alessandro Martino: On 6/27/17, at 9:17 PM, spark pavlov wrote:
> Will this make any sense?
// BOOKING RESPONSE TIME
val influxPreBookingResponseTime = spark.sql("
SELECT brand_name,
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


I would do this:
// BOOKING RESPONSE TIME
val influxPreBookingResponseTime = spark.sql("
SELECT brand_name,
 sales_channel_name,
 trade_group,
 trade_name,
 trade_parent_name,
  xml_booking_login,
 MIN(DATEDIFF( isnull(clientResponseUtcTimestamp,end_utc_timestamp) - isnull(clientRequestUtcTimestamp,start_utc_timestamp) )) AS 'min_response_time',
 MAX(DATEDIFF( isnull(clientResponseUtcTimestamp,end_utc_timestamp) - isnull(clientRequestUtcTimestamp,start_utc_timestamp) )) AS 'max_response_time,
 AVG(DATEDIFF( isnull(clientResponseUtcTimestamp,end_utc_timestamp) - isnull(clientRequestUtcTimestamp,start_utc_timestamp) )) AS 'avg_response_time
FROM Booking
GROUP BY brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login")
.withColumn("time", time)

also afaik DATEDIFF returns difference in day, we need milliseconds so you might have to convert datetime values to unix_timestamp and that do the algebraic difference.
[6/28/17, 9:42:48 AM] Alessandro Martino: On 6/27/17, at 9:22 PM, spark pavlov wrote:
> //Booking
select *
from BookRequest br, SalesChannel s, Brand b,Trade t
where br.trade_id == t.trade_id and br.brand_id==b.brand_id and br.sales_channel_id=s.sales_channel_id
LEFT JOIN QueryProxyRequest
on BookRequest.query_uuid==QueryProxyRequest.query_uuid
as Booking

is this query used in the stream to save data in cassandra?


[6/28/17, 9:44:34 AM] Alessandro Martino: On 6/27/17, at 9:22 PM, spark pavlov wrote:
> //success
select '11:35' as 'timestamp', COUNT(query_uuid) AS 'booking_success', brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login
from Booking
where success is not null
group by
brand_name, sales_channel_name, trade_group, trade_name, trade_parent_name, xml_booking_login
success can be: true or false