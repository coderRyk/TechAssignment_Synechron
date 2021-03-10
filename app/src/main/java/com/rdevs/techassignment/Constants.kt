package com.rdevs.techassignment


class Constants {

    companion object {

        // API urls
        const val PETS_LIST_URL =
            "https://drive.google.com/uc?export=download&id=1IsHsAfLQIKV0X057ZgS5K-7XE_A-KUo7"

        const val SETTINGS_URL =
            "https://drive.google.com/uc?export=download&id=1UfWgn-7OMzLd4DbzmPIjYEFVHsIr-LTK"

        // Pets List API Constants
        const val KEY_PETS = "pets"
        const val KEY_IMAGE_URL = "image_url"
        const val KEY_TITLE = "title"
        const val KEY_CONTENT_URL = "content_url"
        const val KEY_DATE_ADDED = "date_added"


        // Settings API Constants
        const val KEY_SETTINGS = "settings"
        const val KEY_IS_CHAT_ENABLED = "isChatEnabled"
        const val KEY_IS_CALL_ENABLED = "isCallEnabled"
        const val KEY_WORK_HOURS = "workHours"

        //Preference Constants
        const val PREFS_FNAME = "AssignmentPrefs"


    }


}