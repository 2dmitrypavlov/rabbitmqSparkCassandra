CREATE KEYSPACE jactravel_monitoring_new WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = true;

CREATE TYPE jactravel_monitoring_new.book_room_info (
adults int,
children int,
child_ages list<int>,
meal_basis_id int,
booking_token text,
property_room_type_id int,
price_diff text,
room_count int,
pre_booking_token text
);

CREATE TYPE jactravel_monitoring_new.room_request (
adults int,
children int,
child_ages list<int>
);

CREATE TYPE jactravel_monitoring_new.request_info (
start_utc_timestamp text,
end_utc_timestamp text,
trade_id int,
brand_id int,
sales_channel_id int,
search_geo_level int,
geo_level1_id int,
geo_level2_id int,
geo_level3_ids list<int>,
property_reference_ids list<int>,
property_ids list<int>,
min_star_rating text,
arrival_date text,
duration int,
meal_basis_id int,
rooms frozen<list<frozen<room_request>>>,
room_count int
);

CREATE TYPE jactravel_monitoring_new.response_info (
property_reference_count int,
property_count int,
priced_room_count int,
suppliers_searched list<text>,
success text,
error_message text,
error_stack_trace text
);

CREATE TABLE jactravel_monitoring_new.cmi_request_second (
query_second bigint,
query_uuid text,
client_request_utc_timestamp text,
client_response_utc_timestamp text,
cmi_query_type int,
error_message text,
error_stack_trace text,
forwarded_request_utc_timestamp text,
forwarded_response_utc_timestamp text,
host text,
request_processor int,
request_url text,
request_xml text,
response_xml text,
success text,
supplier_ip text,
xml_booking_login text,
PRIMARY KEY (query_second, query_uuid)
) WITH CLUSTERING ORDER BY (query_uuid ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND comment = ''
AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99PERCENTILE';

CREATE TABLE jactravel_monitoring_new.sales_channel (
sales_channel_id int PRIMARY KEY,
sales_channel text
) WITH bloom_filter_fp_chance = 0.01
       AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND comment = ''
AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99PERCENTILE';

CREATE TABLE jactravel_monitoring_new.trade_search_archive (
search_date date,
trade_id int,
xml_book_login text,
search_hour tinyint,
request_processor text,
brand_id int,
sales_channel_id int,
search_geo_level text,
geo_level1_id int,
geo_level2_id int,
arrival_date date,
duration tinyint,
adults tinyint,
children tinyint,
success text,
priced_room_count int,
processing_time decimal,
processing_time_p50 decimal,
processing_time_p95 decimal,
processing_time_p99 decimal,
property_count int,
property_reference_count int,
search_count int,
total_time decimal,
total_time_p50 decimal,
total_time_p95 decimal,
total_time_p99 decimal,
PRIMARY KEY (search_date, trade_id, xml_book_login, search_hour, request_processor, brand_id, sales_channel_id, search_geo_level, geo_level1_id, geo_level2_id, arrival_date, duration, adults, children, success)
) WITH CLUSTERING ORDER BY (trade_id ASC, xml_book_login ASC, search_hour ASC, request_processor ASC, brand_id ASC, sales_channel_id ASC, search_geo_level ASC, geo_level1_id ASC, geo_level2_id ASC, arrival_date ASC, duration ASC, adults ASC, children ASC, success ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND comment = ''
AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99PERCENTILE';

CREATE TABLE jactravel_monitoring_new.supplier_book_request_second (
query_second bigint,
query_uuid text,
end_utc_timestamp text,
error_message text,
error_stack_trace text,
host text,
property_count int,
request_count int,
request_xml text,
response_xml text,
source text,
start_utc_timestamp text,
success text,
timeout int,
PRIMARY KEY (query_second, query_uuid)
) WITH CLUSTERING ORDER BY (query_uuid ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND comment = ''
AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99PERCENTILE';

CREATE TABLE jactravel_monitoring_new.brand (
brand_id int PRIMARY KEY,
brand_name text
) WITH bloom_filter_fp_chance = 0.01
       AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND comment = ''
AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99PERCENTILE';

CREATE TABLE jactravel_monitoring_new.supplier_pre_book_request_second (
query_second bigint,
query_uuid text,
end_utc_timestamp text,
error_message text,
error_stack_trace text,
host text,
property_count int,
request_count int,
request_xml text,
response_xml text,
source text,
start_utc_timestamp text,
success text,
timeout int,
PRIMARY KEY (query_second, query_uuid)
) WITH CLUSTERING ORDER BY (query_uuid ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND comment = ''
AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99PERCENTILE';

CREATE TABLE jactravel_monitoring_new.cmi_batch_request_second (
query_second bigint,
query_uuid text,
cmi_query_type int,
error_message text,
error_stack_trace text,
host text,
login text,
property_code text,
request_utc_timestamp text,
request_xml text,
response_utc_timestamp text,
response_xml text,
success text,
supplier_ip text,
PRIMARY KEY (query_second, query_uuid)
) WITH CLUSTERING ORDER BY (query_uuid ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND comment = ''
AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99PERCENTILE';

CREATE TABLE jactravel_monitoring_new.trade (
trade_id int,
booking_country_id int,
selling_country_id int,
booking_country text,
selling_country text,
trade_group text,
trade_name text,
trade_parent_group text,
PRIMARY KEY (trade_id, booking_country_id, selling_country_id)
) WITH CLUSTERING ORDER BY (booking_country_id ASC, selling_country_id ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND comment = ''
AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99PERCENTILE';

CREATE TABLE jactravel_monitoring_new.query_proxy_request_second (
query_second bigint,
query_uuid text,
client_ip text,
client_request_utc_timestamp text,
client_response_utc_timestamp text,
error_message text,
error_stack_trace text,
forwarded_request_utc_timestamp text,
forwarded_response_utc_timestamp text,
host text,
request_processor int,
request_url text,
request_xml text,
response_xml text,
search_query_type int,
success text,
xml_booking_login text,
PRIMARY KEY (query_second, query_uuid)
) WITH CLUSTERING ORDER BY (query_uuid ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND comment = ''
AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99PERCENTILE';

CREATE TABLE jactravel_monitoring_new.cmi_batch_request_updated_second (
query_second bigint,
query_uuid text,
cmi_query_type int,
error_message text,
error_stack_trace text,
host text,
login text,
property_code text,
request_utc_timestamp text,
request_xml text,
response_utc_timestamp text,
response_xml text,
success text,
supplier_ip text,
PRIMARY KEY (query_second, query_uuid)
) WITH CLUSTERING ORDER BY (query_uuid ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND comment = ''
AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99PERCENTILE';

CREATE TABLE jactravel_monitoring_new.supplier_search_request_second (
query_second bigint,
query_uuid text,
end_utc_timestamp text,
error_message text,
error_stack_trace text,
host text,
property_count int,
request_count int,
request_xml text,
response_xml text,
source text,
start_utc_timestamp text,
success text,
timeout int,
PRIMARY KEY (query_second, query_uuid)
) WITH CLUSTERING ORDER BY (query_uuid ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND comment = ''
AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99PERCENTILE';

CREATE TABLE jactravel_monitoring_new.book_request_second (
query_second bigint,
query_uuid text,
arrival_date text,
brand_id int,
currency_id int,
duration int,
end_utc_timestamp text,
error_message text,
error_stack_trace text,
host text,
pre_book_query_uuid text,
property_id int,
rooms frozen<list<frozen<book_room_info>>>,
sales_channel_id int,
search_processor int,
search_query_uuid text,
start_utc_timestamp text,
success text,
trade_id int,
PRIMARY KEY (query_second, query_uuid)
) WITH CLUSTERING ORDER BY (query_uuid ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND comment = ''
AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99PERCENTILE';

CREATE TABLE jactravel_monitoring_new.cmi_request_updated_second (
query_second bigint,
query_uuid text,
client_request_utc_timestamp text,
client_response_utc_timestamp text,
cmi_query_type int,
error_message text,
error_stack_trace text,
forwarded_request_utc_timestamp text,
forwarded_response_utc_timestamp text,
host text,
login text,
property_code text,
request_processor int,
request_url text,
request_xml text,
response_xml text,
success text,
supplier_ip text,
PRIMARY KEY (query_second, query_uuid)
) WITH CLUSTERING ORDER BY (query_uuid ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND comment = ''
AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99PERCENTILE';

CREATE TABLE jactravel_monitoring_new.query_proxy_request_ext (
query_uuid text PRIMARY KEY,
brand_name text,
delta_request_time decimal,
error_stack_trace text,
sales_channel text,
success text,
table_name text,
time_in decimal,
trade_group text,
trade_group_parent text,
trade_name text,
xml_booking_login text
) WITH bloom_filter_fp_chance = 0.01
       AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND comment = ''
AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99PERCENTILE';

CREATE TABLE jactravel_monitoring_new.search_request_second (
query_second bigint,
query_uuid text,
host text,
request_info frozen<request_info>,
response_info frozen<response_info>,
PRIMARY KEY (query_second, query_uuid)
) WITH CLUSTERING ORDER BY (query_uuid ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND comment = ''
AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99PERCENTILE';

CREATE TABLE jactravel_monitoring_new.pre_book_request_second (
query_second bigint,
query_uuid text,
arrival_date text,
brand_id int,
currency_id int,
duration int,
end_utc_timestamp text,
error_message text,
error_stack_trace text,
host text,
property_id int,
rooms frozen<list<frozen<book_room_info>>>,
sales_channel_id int,
search_processor int,
search_query_uuid text,
start_utc_timestamp text,
success text,
trade_id int,
PRIMARY KEY (query_second, query_uuid)
) WITH CLUSTERING ORDER BY (query_uuid ASC)
AND bloom_filter_fp_chance = 0.01
AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
AND comment = ''
AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99PERCENTILE';