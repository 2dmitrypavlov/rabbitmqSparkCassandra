package com.jactravel.monitoring.util

import java.time.{LocalDateTime, ZoneId}
import java.time.format.DateTimeFormatter
import java.time.temporal.{ChronoUnit, TemporalUnit}
import java.util.Date

/**
  * Created by admin on 7/5/17.
  */
object DateTimeUtils {

  def parseDateTime(dateTime: String, temporalUnit: TemporalUnit = ChronoUnit.SECONDS) = {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS]")

    LocalDateTime.parse(dateTime, formatter).truncatedTo(temporalUnit)
  }

  def toDate(dateTime: LocalDateTime) = {
    Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant)
  }

  private[monitoring] def parseDate(date: String) = {
    toDate(parseDateTime(date))
  }

  def datesDiff(date1: String, date2: String, unit: TemporalUnit = ChronoUnit.MILLIS): Long = {
    unit.between(parseDateTime(date1), parseDateTime(date2))
  }

}
