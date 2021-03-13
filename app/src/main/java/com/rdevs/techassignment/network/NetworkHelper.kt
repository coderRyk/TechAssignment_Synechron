package com.rdevs.techassignment.network

import Pet
import android.content.Context
import android.net.ConnectivityManager
import com.rdevs.techassignment.models.PetsListResponse
import com.rdevs.techassignment.models.Settings
import com.rdevs.techassignment.models.SettingsResponse
import com.rdevs.techassignment.utils.UiUtils
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by Rounak Khandeparkar on 3/12/21.
 */
class NetworkHelper private constructor() {


    companion object {

        private val client: OkHttpClient = OkHttpClient()

        @Volatile
        private var INSTANCE: NetworkHelper? = null

        fun getInstance(): NetworkHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NetworkHelper().also {
                    INSTANCE = it
                }
            }

        fun isConnected(context: Context): Boolean {
            val manager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return manager?.activeNetworkInfo != null
        }
    }

    fun getSettingsCall(): Call {

        val request = Request.Builder()
            .url(NetworkConstants.SETTINGS_URL)
            .build()

        return client.newCall(request)
    }

    fun getPetsListCall(): Call {

        val request = Request.Builder()
            .url(NetworkConstants.PETS_LIST_URL)
            .build()

        return client.newCall(request)
    }


    fun handleSettingsResponse(settingsResponse: String): Settings {

        val settingsResponseObj: SettingsResponse =
            Json { ignoreUnknownKeys = true }.decodeFromString(settingsResponse)

        val workHours = settingsResponseObj.settings.workHours;
        val hrsDetails = workHours?.split(" ")

        val startTimeHrs = hrsDetails?.get(1);
        val endTimeHrs = hrsDetails?.get(3);

        val isWithinOfficeHrs = UiUtils.isCurrentTimeWithinTimeRange(startTimeHrs, endTimeHrs)

        settingsResponseObj.settings.isWithinOfficeHrs = isWithinOfficeHrs

        return settingsResponseObj.settings
    }

    fun handlePetsListResponse(petsListResponseString: String): java.util.ArrayList<Pet> {
        val petsList: java.util.ArrayList<Pet>

        val petsListResponseObj: PetsListResponse =
            Json.decodeFromString(petsListResponseString)

        petsList =
            if (!petsListResponseObj.pets.isNullOrEmpty()) petsListResponseObj.pets as java.util.ArrayList<Pet> else ArrayList<Pet>();

        return petsList
    }

}