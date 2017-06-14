package com.jactravel.monitoring.model

/**
  * Created by admin on 6/13/17.
  */
case class RoomRequest(
                        adults: Int
                        , children: Int
                        , childAges: List[Int]
                      )
