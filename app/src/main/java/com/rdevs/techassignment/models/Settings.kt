package com.rdevs.techassignment.models

import kotlinx.serialization.Serializable

/**
 * Created by Rounak Khandeparkar on 3/12/21.
 */
@Serializable
class Settings {
    val isChatEnabled: Boolean = false
    val isCallEnabled: Boolean = false
    val workHours: String? = null
    var isWithinOfficeHrs: Boolean = false
}