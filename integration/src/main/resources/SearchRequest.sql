-- RICH SEARCH REQUEST
SELECT sr.query_uuid,
       brand_name,
       trade_name,
       trade_group,
       trade_parent_group,
       sales_channel,
       (unix_timestamp(request_info.end_utc_timestamp) - unix_timestamp(request_info.start_utc_timestamp)) * 1000 as response_time_ms,
       sr.response_info.error_stack_trace,
       sr.response_info.success,
       xml_booking_login,
       window(request_info.start_utc_timestamp, '5 minutes').end as time
FROM PureSearchRequest as sr,
     SalesChannel as sc,
     Trade as t,
     Brand as b
LEFT JOIN QueryProxyRequest as qpr
ON sr.query_uuid == qpr.query_uuid
WHERE sr.request_info.sales_channel_id == sc.sales_channel_id
AND sr.request_info.trade_id == t.trade_id
AND sr.request_info.brand_id == b.brand_id

-- SEARCH COUNT
SELECT COUNT(query_uuid) as search_count,
    time,
    brand_name,
    sales_channel,
    trade_group,
    trade_name,
    trade_parent_group,
    xmL_booking_login
FROM RichSearchRequest
GROUP BY
    time,
    brand_name,
    sales_channel,
    trade_group,
    trade_name,
    trade_parent_group,
    xml_booking_login

-- SEARCH SUCCESS
SELECT COUNT(query_uuid) as search_success,
    time,
    brand_name,
    sales_channel,
    trade_group,
    trade_name,
    trade_parent_group,
    xmL_booking_login
FROM RichSearchRequest
WHERE success IS NOT NULL
GROUP BY
    time,
    brand_name,
    sales_channel,
    trade_group,
    trade_name,
    trade_parent_group,
    xml_booking_login

-- SEARCH ERROR
SELECT COUNT(query_uuid) as search_errors,
    time,
    brand_name,
    sales_channel,
    trade_group,
    trade_name,
    trade_parent_group,
    xml_booking_login
FROM RichSearchRequest
WHERE error_stack_trace IS NOT NULL
GROUP BY
    time,
    brand_name,
    sales_channel,
    trade_group,
    trade_name,
    trade_parent_group,
    xml_booking_login

-- SEARCH RESPONSE TIME
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
FROM RichSearchRequest
GROUP BY
    time,
    brand_name,
    sales_channel,
    trade_group,
    trade_name,
    trade_parent_group,
    xml_booking_login