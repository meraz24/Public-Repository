package com.passportv3locity.utils

import android.text.format.DateUtils
import com.passportv3locity.common.Constants
import junit.runner.BaseTestRunner.truncate
import java.text.DateFormatSymbols
import java.text.Format
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateTimeHelperElapsed {

    var ONE_DAY_TIME = (1000 * 60 * 60 * 24).toLong()

    fun _getTodayMidnightTime(): Long {
        return org.apache.commons.lang3.time.DateUtils.truncate(Date(), Calendar.DAY_OF_MONTH)
            .getTime()
    }

    fun isToday(date: Date): Boolean {
        val todayMidnight = _getTodayMidnightTime()
        return todayMidnight <= date.time && date.time < todayMidnight + ONE_DAY_TIME
    }


    fun isYesterday(d: Date): Boolean {
        return DateUtils.isToday(d.time + DateUtils.DAY_IN_MILLIS)
    }


    fun isInLastWeek(date: Date): Boolean {
        val todayMidnight = _getTodayMidnightTime()
        return todayMidnight - ONE_DAY_TIME * 6 < date.time && date.time < todayMidnight
    }


    fun getDateOfWeek(date: Date?): String? {
        val c = Calendar.getInstance()
        c.time = date
        val dayOfWeek = c[Calendar.DAY_OF_WEEK]
        when (dayOfWeek) {
            Calendar.SUNDAY -> return "Sunday"
            Calendar.MONDAY -> return "Monday"
            Calendar.TUESDAY -> return "Tuesday"
            Calendar.WEDNESDAY -> return "Wednesday"
            Calendar.THURSDAY -> return "Thursday"
            Calendar.FRIDAY -> return "Friday"
            Calendar.SATURDAY -> return "Saturday"
        }
        return ""
    }


    fun toString(milli: Long, timeFormat: String?): String? {
        val date = Date(milli)
        val simpleDateFormat = getSimpleDateFormat(timeFormat)
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("ITC"));
        val symbols = DateFormatSymbols(Locale.getDefault())
        symbols.amPmStrings = arrayOf("AM", "PM")
        simpleDateFormat.dateFormatSymbols = symbols
        return simpleDateFormat.format(date)
    }

    fun toStringtime(milli: Long): String? {
        val date = Date(milli)
        val simpleDateFormat = getSimpleDateFormat("dd-MMM,yyyy, hh:mm a")
        return simpleDateFormat.format(date).replace("am", "AM").replace("pm", "PM")
    }

    fun convertTimeTwo(time: Long): String? { // DEC 01, 00:00 AM convert for long to string
        val date = Date(time)
        val format: Format = SimpleDateFormat(" yyyy MMM,hh:mm a")
        val symbols = DateFormatSymbols(Locale.getDefault())
        symbols.amPmStrings = arrayOf("am", "pm")
        (format as SimpleDateFormat).dateFormatSymbols = symbols
        return format.format(date)
    }

    fun getSimpleDateFormat(dateFormat: String?): SimpleDateFormat {
        val simpleDateFormat = SimpleDateFormat(dateFormat,Locale.getDefault())
        return simpleDateFormat
    }

    fun changeDateFormat(date: String?, outputDateFormat: String, shouldShowDateOnly: Boolean): String? {
        var finalDate: String? = null
        try {
            //Input date format
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.getDefault())
            val objDate = dateFormat.parse(date)

            //Output date format
            val dateFormat2 = SimpleDateFormat(outputDateFormat)
            val convertDate = dateFormat2.format(objDate)

            if (shouldShowDateOnly) {
                finalDate = if (isToday(objDate)) {
                    "Today"
                } else if (isYesterday(objDate)) {
                    "Yesterday"
                } else {
                    convertDate
                    //finalDate = convertDate.replace("am", "AM").replace("pm", "PM")
                }
            } else {
                finalDate = dateFormat2.format(objDate)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return finalDate
    }

    fun changeDateWithCheckTodayYesterday(date: String?): String? {
        var finalDate: String? = null
        try {
            //current date format
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.getDefault())
            val objDate = dateFormat.parse(date)
            // Expected date format
            val dateFormat2 = SimpleDateFormat("MMM dd")
            val convertDate = dateFormat2.format(objDate)
            finalDate = if (isToday(objDate)) {
                "Today"
            } else if (isYesterday(objDate)) {
                "Yesterday"
            } else {
                convertDate
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return finalDate
    }

    fun getElapsedTime(from: Long): String? {
        val result = StringBuilder("")
        val date = Date(from) //convertFromUTCToLocaltimezone(new Date(from));
        if (isToday(date)) {
            val elapsedMillis = System.currentTimeMillis() - date.time
            val millisPerMinute = (1000 * 60).toLong()
            val millisPerHour = millisPerMinute * 60
            val elapsedHours = elapsedMillis / millisPerHour
            val elapsedMinutes = elapsedMillis / millisPerMinute
            if (elapsedHours > 0) result.append(elapsedHours).append(" hr")
                .append(if (elapsedHours > 1) "s" else "")
                .append(" ago") else if (elapsedMinutes > 0) result.append(elapsedMinutes)
                .append(" min").append(if (elapsedMinutes > 1) "s" else "")
                .append(" ago") else result.append("Just now")
        } else if (isYesterday(date)) {
            result.append("Yesterday")
            result.append(" at ").append(getSimpleDateFormat("hh:mm a").format(date))
        } else if (isInLastWeek(date)) {
            result.append(getDateOfWeek(date))
            result.append(" at ").append(getSimpleDateFormat("hh:mm a").format(date))
        } else {
            result.append(getSimpleDateFormat("MMM dd, hh:mm a").format(date))
        }
        return result.toString()
    }

    fun getElapsedTimeForNotification(from: Long): String? {
        val result = StringBuilder("")
        val date = Date(from) //convertFromUTCToLocaltimezone(new Date(from));
        if (isToday(date)) {
            result.append("Today")
            result.append(", ").append(getSimpleDateFormat("hh:mm a").format(date))
        } else if (isYesterday(date)) {
            result.append("Yesterday")
            result.append(", ").append(getSimpleDateFormat("hh:mm a").format(date))
        } else {
            result.append(getSimpleDateFormat("MMM dd, hh:mm a").format(date))
        }
        return result.toString()
    }


    fun firstTwo(str: String): String? {
        return if (str.length < 2) str else str.substring(0, 2)
    }


    fun convertUTC2Local(utcTime: String?): String? {
        var time = ""
        if (utcTime != null) {
            val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC"))
            var gpsUTCDate: Date? = null //from  ww  w.j  a va 2 s  . c  o  m
            try {
                gpsUTCDate = utcFormatter.parse(utcTime)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val localFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            localFormatter.setTimeZone(TimeZone.getDefault())
            assert(gpsUTCDate != null)
            time = localFormatter.format(gpsUTCDate?.getTime())
        }
        return time
    }

}