package com.rdevs.techassignment

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rdevs.techassignment.utils.PrefsHelper
import com.rdevs.techassignment.utils.UiUtils
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.rdevs.techassignment", appContext.packageName)
    }


    @Test
    fun testPreferencesHelper() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val prefsHelper: PrefsHelper = PrefsHelper.getInstance(appContext)

        val TEST_KEY = "TEST_KEY"
        var TEST_VALUE :String? = ""

        prefsHelper.savePreferences(TEST_KEY, TEST_VALUE)
        assertEquals(prefsHelper.getPreferences(TEST_KEY), "")

        TEST_VALUE = "TEST_VALUE_1"
        prefsHelper.savePreferences(TEST_KEY, TEST_VALUE)
        assertEquals(prefsHelper.getPreferences(TEST_KEY), TEST_VALUE)

        TEST_VALUE = "TEST_VALUE_2"
        prefsHelper.savePreferences(TEST_KEY, TEST_VALUE)
        assertNotEquals(prefsHelper.getPreferences(TEST_KEY), "TEST_VALUE")

        TEST_VALUE = null
        prefsHelper.savePreferences(TEST_KEY, TEST_VALUE)
        assertNull(prefsHelper.getPreferences(TEST_KEY))

        TEST_VALUE = "TEST_VALUE_3"
        prefsHelper.savePreferences(TEST_KEY, TEST_VALUE)
        assertNotNull(prefsHelper.getPreferences(TEST_KEY))

    }


}