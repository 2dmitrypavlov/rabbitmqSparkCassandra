package com.jactravel.monitoring.model.rabbit

/**
  * Created by dmitry on 7/18/17.
  */
case class RoomRequest(
                        adults: Int,
                        children: Int,
                        childAges: List[Int]
                      )
