package com.realtyagency.tm.app.platform

import androidx.annotation.StringRes

data class Action(@StringRes val text: Int, val func: () -> Unit = {})