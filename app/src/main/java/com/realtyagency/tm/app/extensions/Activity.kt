package com.realtyagency.tm.app.extensions

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt

fun Activity.showToast(text: String?) {
    text?.let {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}

fun Activity.setStatusBarColor(@ColorInt color: Int) {
    window.statusBarColor = color
}

fun Activity.setStatusBarLightMode(
    isLightMode: Boolean
) {
    val decorView = this.window.decorView
    var vis = decorView.systemUiVisibility
    vis = if (isLightMode) {
        vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
    decorView.systemUiVisibility = vis
}
