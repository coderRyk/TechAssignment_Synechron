package com.rdevs.techassignment.utils

import android.view.View

class UiUtils {

    companion object {
        fun toggleVisibility(view: View?, showView: Boolean) {
            if (view != null) {
                view.visibility = if (showView) View.VISIBLE else View.GONE
            }
        }
    }

}