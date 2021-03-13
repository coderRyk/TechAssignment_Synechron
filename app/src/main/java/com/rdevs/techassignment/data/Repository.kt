package com.rdevs.techassignment.data

import Pet
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rdevs.techassignment.models.Settings
import com.rdevs.techassignment.network.NetworkHelper
import com.rdevs.techassignment.utils.PrefsHelper
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * Created by Rounak Khandeparkar on 3/12/21.
 */
class Repository private constructor(val netHelper: NetworkHelper, val prefsHelper: PrefsHelper) {

    companion object {

        @Volatile
        var INSTANCE: Repository? = null

        fun getInstance(
            netHelper: NetworkHelper,
            prefsHelper: PrefsHelper
        ): Repository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Repository(netHelper, prefsHelper).also {
                    INSTANCE = it
                }
            }
    }

    val petsListLiveData: MutableLiveData<List<Pet>> = MutableLiveData()

    val settingsLiveData: MutableLiveData<Settings> = MutableLiveData()

    //region Pets list
    fun getPetsList(): LiveData<List<Pet>> {

        val petsListResponse =
            prefsHelper.getPreferences(PrefsHelper.PREF_KEY_PETS_LIST)

        if (petsListResponse.isNullOrEmpty()) {

            netHelper.getPetsListCall().enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    petsListLiveData.postValue(null)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        var petsResponse = response.body()!!.string()

                        Log.d("fetchPetsList ", "Response string $petsResponse")
                        prefsHelper
                            .savePreferences(PrefsHelper.PREF_KEY_PETS_LIST, petsResponse)

                        petsListLiveData.postValue(
                            netHelper.handlePetsListResponse(petsResponse)
                        )

                    } else {
                        petsListLiveData.postValue(null)
                    }
                }
            })
        } else {
            petsListLiveData.value =
                netHelper.handlePetsListResponse(petsListResponse)
        }

        return petsListLiveData
    }

    //endregion

    //region Settings

    fun getSettings(): LiveData<Settings> {

        val settingsResponse =
            prefsHelper.getPreferences(PrefsHelper.PREF_KEY_SETTINGS)


        if (settingsResponse.isNullOrEmpty()) {

            netHelper.getSettingsCall().enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    settingsLiveData.postValue(null)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val settingsAPIResponse = response.body()!!.string()
                        Log.d("fetchSettings ", "Response string $settingsAPIResponse")

                        prefsHelper.savePreferences(
                            PrefsHelper.PREF_KEY_SETTINGS,
                            settingsAPIResponse
                        )

                        settingsLiveData.postValue(
                            netHelper.handleSettingsResponse(settingsAPIResponse)
                        )
                    } else {
                        settingsLiveData.postValue(null)
                    }
                }
            })

        } else {
            settingsLiveData.postValue(netHelper.handleSettingsResponse(settingsResponse))
        }


        return settingsLiveData
    }

    //endregion

}