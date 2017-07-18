package com.jactravel.monitoring.model.rabbit

/**
  * Created by dmitry on 7/18/17.
  */
case class QueryProxyRequestExt(
                                 override val queryUuid: String,
                                 tableName: Option[String] = None
                               ) extends CommonRequest
