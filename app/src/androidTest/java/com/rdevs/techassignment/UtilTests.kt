package com.rdevs.techassignment

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rdevs.techassignment.utils.Utils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Created by Rounak Khandeparkar on 3/15/21.
 */
@RunWith(AndroidJUnit4::class)
class UtilTests {

    @Test
    fun testTimeRangeHelperMethod() {


        val calendar = Calendar.getInstance(Locale.getDefault());

        calendar.set(Calendar.HOUR_OF_DAY, 8)

        var isWithinRange = Utils.isTimeWithinRange(calendar, "06:00", "07:00")
        assertFalse(isWithinRange)

        isWithinRange = Utils.isTimeWithinRange(calendar, "06:00", "09:00")
        assertTrue(isWithinRange)

        isWithinRange = Utils.isTimeWithinRange(calendar, "08:00", "08:01")
        assertFalse(isWithinRange)

        isWithinRange = Utils.isTimeWithinRange(calendar, "08:00", "08:00")
        assertFalse(isWithinRange)

        calendar.set(Calendar.HOUR_OF_DAY, 22)
        isWithinRange = Utils.isTimeWithinRange(calendar, "05:00", "023:30")
        assertTrue(isWithinRange)

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        isWithinRange = Utils.isTimeWithinRange(calendar, "00:00", "01:00")
        assertTrue(isWithinRange)

    }


}