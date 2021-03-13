package com.rdevs.techassignment.utils

import android.view.View
import java.text.SimpleDateFormat
import java.util.*

class UiUtils {

    companion object {
        fun toggleVisibility(view: View?, showView: Boolean) {
            if (view != null) {
                view.visibility = if (showView) View.VISIBLE else View.GONE
            }
        }

        fun isCurrentTimeWithinTimeRange(startTime: String?, endTime: String?): Boolean {

            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())

            val calendar: Calendar = Calendar.getInstance(Locale.getDefault());
            val currentHrs = calendar.get(Calendar.HOUR_OF_DAY);

            calendar.time = formatter.parse(startTime)

            val startHrs = calendar.get(Calendar.HOUR_OF_DAY);

            calendar.time = formatter.parse(endTime)

            val endHrs = calendar.get(Calendar.HOUR_OF_DAY);

            return currentHrs in startHrs..endHrs;

        }
    }

}