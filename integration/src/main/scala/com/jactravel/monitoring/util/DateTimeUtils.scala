package com.jactravel.monitoring.util

import java.time.{ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter
import java.time.temporal.{ChronoUnit, TemporalUnit}
import java.util.Date

/**
  * Created by admin on 7/5/17.
  */
object DateTimeUtils {

  def parseDateTime(dateTime: String, temporalUnit: TemporalUnit = ChronoUnit.SECONDS) = {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS]").withZone(ZoneId.of("UTC"))

    ZonedDateTime.parse(dateTime, formatter)
  }

  def toDate(dateTime: ZonedDateTime) = {
    Date.from(dateTime.toInstant)
  }

  private[monitoring] def parseDate(date: String) = {
    toDate(parseDateTime(date))
  }

  def datesDiff(date1: String, date2: String, unit: TemporalUnit = ChronoUnit.MILLIS): Long = {
    unit.between(parseDateTime(date1), parseDateTime(date2))
  }

}
