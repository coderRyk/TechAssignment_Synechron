package com.rdevs.techassignment.models

import Pet
import kotlinx.serialization.Serializable

/**
 * Created by Rounak Khandeparkar on 3/12/21.
 */
@Serializable
data class PetsListResponse(val pets: List<Pet>)
