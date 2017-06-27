//count


select count(b.queryUUID) from BookRequest
where b.trade_id==t.trade_id /
group by
tradeID,
brandID,
salesChannelID;



val df = BR
.join(TC, BR.trade_id == TC.trade_id
.select(BR.queryUUID,
	BR.startUtcTimestamp,
	BR.endUtcTimestamp,
	mapped(BR.brandID).as(brand_name),
	mapped(BR.salesChannelID).as(sales_channel_name),
	(BR.success).as[Boolean],				// if success == "true" true else false
	(BR.errorStackTrace).as[Boolean],			// if errorStackTrace > 0 true else false
	TC.trade_group,
	TC.trade_name,
	TC.trade_parent_name
)

// INFLUX ENTITY
 {
	QueryUUID: UUID,
	ResponseTime: Timestamp or Long,		// EndUtcTimestamp - StartUtcTimeStamp
	BrandName: String,
	SalesChannelName: String,
	TradeGroup: String,
	TradeName: String,
	TradeParentName: String,
	time: Timestamp,				// Influxd time
	Success: Boolean,
	ErrorStackTrace: Boolean
 }

// BOOKING COUNT
SELECT COUNT (QueryUUID) FROM df
WHERE time > 'lower_bound' AND time < 'upper_bound'
AND BrandName = COALESCE("SomeString", BrandName)
AND SalesChannel = COALESCE("SomeString", SalesChannel)
AND TradeParentGroup = COALESCE("SomeString", TradeParentGroup)
AND TradeGroup = COALESCE("SomeString", TradeGroup)
AND TradeName = COALESCE("SomeString", TradeName);

// BOOK RESPONSE TIME
SELECT ResponseTime FROM df
WHERE time > 'lower_bound' AND time < 'upper_bound'
AND BrandName = COALESCE("SomeString", BrandName)
AND SalesChannel = COALESCE("SomeString", SalesChannel)
AND TradeParentGroup = COALESCE("SomeString", TradeParentGroup)
AND TradeGroup = COALESCE("SomeString", TradeGroup)
AND TradeName = COALESCE("SomeString", TradeName);

// BOOK SUCCESS COUNT
SELECT COUNT (QueryUUID) FROM df
WHERE time > 'lower_bound' AND time < 'upper_bound'
AND Success == false
AND ErrorStackTrace == true
AND BrandName = COALESCE("SomeString", BrandName)
AND SalesChannel = COALESCE("SomeString", SalesChannel)
AND TradeParentGroup = COALESCE("SomeString", TradeParentGroup)
AND TradeGroup = COALESCE("SomeString", TradeGroup)
AND TradeName = COALESCE("SomeString", TradeName);

// BOOK EXCEPTION COUNT
SELECT COUNT (QueryUUID) FROM df
WHERE time > 'lower_bound' AND time < 'upper_bound'
AND ErrorStackTrace == true
AND BrandName = COALESCE("SomeString", BrandName)
AND SalesChannel = COALESCE("SomeString", SalesChannel)
AND TradeParentGroup = COALESCE("SomeString", TradeParentGroup)
AND TradeGroup = COALESCE("SomeString", TradeGroup)
AND TradeName = COALESCE("SomeString", TradeName);