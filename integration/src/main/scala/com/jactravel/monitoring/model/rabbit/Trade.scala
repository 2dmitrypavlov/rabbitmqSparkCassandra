package com.jactravel.monitoring.model.rabbit

/**
  * Created by admin on 7/5/17.
  */
case class Trade(
                  trade_id: Int,
                  trade_name: Option[String],
                  trade_group: Option[String],
                  trade_parent_group: Option[String],
                  booking_country_id: Option[String],
                  booking_country: Option[String],
                  selling_country_id: Option[String],
                  selling_country: Option[String]


                )
