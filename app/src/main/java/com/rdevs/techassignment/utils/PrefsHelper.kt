package com.rdevs.techassignment.utils

import android.content.Context
import android.content.SharedPreferences

class PrefsHelper private constructor(private val context: Context) {

    companion object {

        const val PREF_KEY_SETTINGS = "SETTINGS"
        const val PREF_KEY_PETS_LIST = "PETS_LIST"
        const val PREFS_FNAME = "AssignmentPrefs"

        @Volatile
        private var INSTANCE: PrefsHelper? = null

        fun getInstance(context: Context): PrefsHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PrefsHelper(context).also {
                    INSTANCE = it
                }
            }

    }

    fun savePreferences(key: String, dataToSave: String?) {
        val preferences = context.getSharedPreferences(PREFS_FNAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(key, dataToSave)
        editor.apply()
        editor.commit()
    }

    fun getPreferences(key: String): String? {
        val preferences =
            context.getSharedPreferences(PREFS_FNAME, Context.MODE_PRIVATE)
        return preferences.getString(key, null);
    }

}