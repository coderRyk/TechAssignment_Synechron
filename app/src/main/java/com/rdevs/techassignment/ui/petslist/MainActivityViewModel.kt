package com.rdevs.techassignment.ui.petslist

import Pet
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rdevs.techassignment.data.Repository
import com.rdevs.techassignment.models.Settings
import com.rdevs.techassignment.network.NetworkHelper
import com.rdevs.techassignment.utils.PrefsHelper


/**
 * Created by Rounak Khandeparkar on 3/12/21.
 */
open class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: Repository? = null
    var isLoading = MutableLiveData<Boolean>()

    var petsListLiveData: LiveData<List<Pet>> = MutableLiveData()
    var settingsLiveData: LiveData<Settings> = MutableLiveData()

    init {
        initViewModel()
    }

    fun initViewModel() {
        repository = Repository.getInstance(
            NetworkHelper.getInstance(),
            PrefsHelper.getInstance(getApplication())
        )

        fetchSettings()
        fetchPetsList()
    }


    fun fetchPetsList() {
        isLoading.postValue(true)
        petsListLiveData = repository!!.getPetsList()
    }

    fun fetchSettings() {
        isLoading.postValue(true)
        settingsLiveData = repository!!.getSettings()
    }


}