package com.rdevs.techassignment.utils

import android.view.View


fun View.toggleVisibility(showView: Boolean) {
    if (this != null) {
        visibility = if (showView) View.VISIBLE else View.GONE
    }
}