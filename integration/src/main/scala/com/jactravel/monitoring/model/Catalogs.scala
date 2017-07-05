package com.jactravel.monitoring.model

/**
  * Created by admin on 7/5/17.
  */
case class Trade(
                  tradeId: Int,
                  bookingCountryId: Option[Int],
                  sellingCountryId: Option[Int],
                  bookingCountry: Option[String],
                  sellingCountry: Option[String],
                  tradeGroup: Option[String],
                  tradeName: Option[String],
                  tradeParentGroup: Option[String]
                )

case class Brand(
                  brandId: Int,
                  brandName: String
                )

case class SalesChannel(
                         salesChannelId: Int,
                         salesChannel: String
                       )