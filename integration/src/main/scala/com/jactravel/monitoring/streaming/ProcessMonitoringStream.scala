package com.jactravel.monitoring.streaming

import com.jactravel.monitoring.model.ClientSearch
import com.rabbitmq.client.QueueingConsumer.Delivery

/**
  * Created by eugen on 5/30/17.
  */
trait ProcessMonitoringStream {

  def messageHandler(delivery: Delivery) : ClientSearch = {
    delivery.getBody
  }

}
