package com.jactravel.monitoring.streaming

import com.jactravel.monitoring.model.ClientSearch
import com.rabbitmq.client.QueueingConsumer.Delivery
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by eugen on 5/30/17.
  */
trait ProcessMonitoringStream extends LazyLogging {

  def messageHandler(delivery: Delivery) : ClientSearch = {
    logger.info(new String(delivery.getBody))
    ClientSearch(SearchQueryUUID = "1")
  }

}
