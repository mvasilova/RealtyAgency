package com.realtyagency.tm.app.platform

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import java.util.*

class ResourceManager(val context: Context) {
    fun getString(@StringRes resId: Int): String = context.resources.getString(resId)

    fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String =
        context.resources.getString(resId, *formatArgs)

    fun getStringArray(@ArrayRes resId: Int): List<String> {
        val config = context.resources.configuration
        config.setLocale(Locale.getDefault())
        return context.createConfigurationContext(config).resources.getStringArray(resId).asList()
    }
}