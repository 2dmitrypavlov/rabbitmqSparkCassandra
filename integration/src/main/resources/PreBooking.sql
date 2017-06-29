-- Pre-Booking
SELECT br.search_query_uuid as query_uuid,
       brand_name,
       trade_name,
       trade_group,
       trade_parent_group,
       sales_channel,
       (unix_timestamp(end_utc_timestamp) - unix_timestamp(start_utc_timestamp)) * 1000 as response_time_ms,
       error_stack_trace,
       success,
       xml_booking_login,
       window(start_utc_timestamp, '5 minutes').end
FROM PreBookRequest as pbr,
     SalesChannel as sc,
     Trade as t,
     Brand as b
LEFT JOIN QueryProxyRequest as qpr
ON pbr.search_query_uuid == qpr.query_uuid
WHERE pbr.sales_channel_id == sc.sales_channel_id
AND pbr.trade_id == t.trade_id
AND pbr.brand_id == b.brand_id


-- Pre-booking count
SELECT COUNT(query_uuid) AS pre_booking_count,
       time,
       brand_name,
       sales_channel,
       trade_group,
       trade_name,
       trade_parent_group,
       xmL_booking_login
FROM PreBookingEnriched
GROUP BY
    time,
    brand_name,
    sales_channel,
    trade_group,
    trade_name,
    trade_parent_group,
    xml_booking_login

-- Pre-booking success
SELECT COUNT(query_uuid) as pre_booking_success,
       time,
       brand_name,
       sales_channel,
       trade_group,
       trade_name,
       trade_parent_group,
       xml_booking_login
FROM PreBookingEnriched
WHERE success IS NOT NULL
GROUP BY
    time,
    brand_name,
    sales_channel,
    trade_group,
    trade_name,
    trade_parent_group,
    xml_booking_login

-- Pre-booking error
SELECT COUNT(query_uuid) AS pre_booking_errors,
    time,
    brand_name,
    sales_channel,
    trade_group,
    trade_name,
    trade_parent_group,
    xmL_booking_login
FROM PreBookingEnriched
GROUP BY
    time,
    brand_name,
    sales_channel,
    trade_group,
    trade_name,
    trade_parent_group,
    xml_booking_login

-- Pre-booking response time
SELECT time,
       brand_name,
       sales_channel,
       trade_group,
       trade_name,
       trade_parent_group,
       xmL_booking_login,
       min(response_time_ms) as min_response_time_ms,
       max(response_time_ms) as max_response_time_ms,
       avg(response_time_ms) as avg_response_time_ms
FROM PreBookingEnriched
GROUP BY
    time,
    brand_name,
    sales_channel,
    trade_group,
    trade_name,
    trade_parent_group,
    xml_booking_login
