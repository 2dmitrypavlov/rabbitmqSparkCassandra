CREATE TABLE jactravel_monitoring.brand (
  brand_id int,
  brand_name text,
  PRIMARY KEY (brand_id)
);

insert into brand(brand_id, brand_name) values(1, 'Bookatrip');
insert into brand(brand_id, brand_name) values(2, 'Wholesale');
insert into brand(brand_id, brand_name) values(3,'Reservwire feed');
insert into brand(brand_id, brand_name) values(4,'TotalStay UK');
insert into brand(brand_id, brand_name) values(5,'TotalStay CH');
insert into brand(brand_id, brand_name) values(6,'TotalStay DK');
insert into brand(brand_id, brand_name) values(7,'TotalStay ES');
insert into brand(brand_id, brand_name) values(8,'TotalStay FR');
insert into brand(brand_id, brand_name) values(9,'TotalStay IE');
insert into brand(brand_id, brand_name) values(10,'TotalStay NL');
insert into brand(brand_id, brand_name) values(11,'TotalStay SE');
insert into brand(brand_id, brand_name) values(12,'TotalStay.com');
insert into brand(brand_id, brand_name) values(13,'Exclusively Hotels');
insert into brand(brand_id, brand_name) values(14,'Hotel Pronto');
insert into brand(brand_id, brand_name) values(15,'Hotel Pronto Affiliated');

CREATE TABLE jactravel_monitoring.sales_channel (
  sales_channel_id int,
  sales_channel text,
  PRIMARY KEY (sales_channel_id)
);

insert into sales_channel(sales_channel_id, sales_channel) values(1, 'Call Centre');
insert into sales_channel(sales_channel_id, sales_channel) values(2, 'Web');
insert into sales_channel(sales_channel_id, sales_channel) values(3, 'XML - Trade');
insert into sales_channel(sales_channel_id, sales_channel) values(4, 'XML Amadeus');
insert into sales_channel(sales_channel_id, sales_channel) values(5, 'XML Gateway');
insert into sales_channel(sales_channel_id, sales_channel) values(6, 'Affiliates');
insert into sales_channel(sales_channel_id, sales_channel) values(7, 'XML BCD');
insert into sales_channel(sales_channel_id, sales_channel) values(9, 'Groups');
insert into sales_channel(sales_channel_id, sales_channel) values(10, 'XML-B2B2C');
insert into sales_channel(sales_channel_id, sales_channel) values(11, 'XML3P DirectB2B');
insert into sales_channel(sales_channel_id, sales_channel) values(12, 'XML3P DirectB2C');

CREATE TABLE jactravel_monitoring.trade (
trade_id int,
booking_country_id int,
selling_country_id int,
booking_country text,
selling_country text,
trade_group text,
trade_name text,
trade_parent_group text,
PRIMARY KEY (trade_id, booking_country_id, selling_country_id)
);

COPY jactravel_monitoring.trade (trade_id, trade_name, trade_group,	trade_parent_group,	booking_country_id,	booking_country, selling_country_id, selling_country) from 'Trade.txt';