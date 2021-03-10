package com.rdevs.techassignment.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.rdevs.techassignment.Constants
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class PrefsHelper {

    companion object {

        const val PREF_KEY_SETTINGS = "SETTINGS"
        const val PREF_KEY_PETS_LIST = "PETS_LIST"

        fun savePreferences(context: Context, key: String, dataToSave: String?) {
            val preferences =
                context.getSharedPreferences(Constants.PREFS_FNAME, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.putString(key, dataToSave)
            editor.apply()
            editor.commit()
        }

        fun getPreferences(context: Context, key: String): String? {
            val preferences =
                context.getSharedPreferences(Constants.PREFS_FNAME, Context.MODE_PRIVATE)
            return preferences.getString(key, null);
        }


        fun getOfficeHrs(context: Context): String? {
            val preferences: String? = getPreferences(context, PREF_KEY_SETTINGS);
            val configsResponseObject = JSONObject(preferences)

            var settings: JSONObject = configsResponseObject.getJSONObject(Constants.KEY_SETTINGS)
            return settings.getString(Constants.KEY_WORK_HOURS);
        }

        fun getPetsListResponse(context: Context): String? {
            return getPreferences(context, PREF_KEY_PETS_LIST);
        }

        fun getSettingsResponse(context: Context): String? {
            return getPreferences(context, PREF_KEY_SETTINGS);
        }

        fun isDataSet(context: Context): Boolean {
            val settingsResponse = getPreferences(context, PREF_KEY_SETTINGS)
            val petsListResponse = getPreferences(context, PREF_KEY_PETS_LIST)
            return !settingsResponse.isNullOrEmpty() && !petsListResponse.isNullOrBlank()
        }

        fun isSupportAvailabile(context: Context) :Boolean {

            val officeHrs = getOfficeHrs(context)

            // Sample hrs = M-F 9:00 - 18:00
            val hrsDetails = officeHrs?.split(" ")

            val startHrsDigits = hrsDetails?.get(1);
            val endHrsDigits = hrsDetails?.get(3);

            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())

            val calendar: Calendar = Calendar.getInstance(Locale.getDefault());
            val currentHrs = calendar.get(Calendar.HOUR_OF_DAY);

            calendar.time = formatter.parse(startHrsDigits)

            val startHrs = calendar.get(Calendar.HOUR_OF_DAY);

            calendar.time = formatter.parse(endHrsDigits)

            val endHrs = calendar.get(Calendar.HOUR_OF_DAY);

            val supportEnabled = currentHrs in startHrs..endHrs

            Log.d("handleChatAndCallClick", "$startHrs::  $endHrs::  $currentHrs $supportEnabled")

            return supportEnabled;

        }

    }
}