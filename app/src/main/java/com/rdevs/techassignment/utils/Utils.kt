package com.rdevs.techassignment.utils

import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object {

        fun isTimeWithinRange(calendar: Calendar, startTime: String?, endTime: String?): Boolean {

            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())

            val from = Calendar.getInstance()
            from[Calendar.HOUR_OF_DAY] = formatter.parse(startTime).hours
            from[Calendar.MINUTE] = 0

            val to = Calendar.getInstance()
            to[Calendar.HOUR_OF_DAY] = formatter.parse(endTime).hours
            to[Calendar.MINUTE] = 0

            return calendar.after(from) && calendar.before(to)

        }
    }

}