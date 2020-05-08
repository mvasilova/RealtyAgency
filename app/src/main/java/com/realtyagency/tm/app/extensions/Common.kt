package com.realtyagency.tm.app.extensions

import android.content.Context
import android.content.res.Resources
import com.hannesdorfmann.adapterdelegates4.AbsDelegationAdapter
import java.text.SimpleDateFormat
import java.util.*

val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.dpToPx: Float
    get() = (this * Resources.getSystem().displayMetrics.density)


fun Long.toDateFormatGmt(context: Context, pattern: String = "dd MMM yyyy"): String =
    SimpleDateFormat(pattern, Locale.getDefault())
        .apply { timeZone = TimeZone.getTimeZone("GMT") }
        .format(this.checkUnixTimeStamp())

fun Long.checkUnixTimeStamp() = if (this.toString().length <= 10) this * 1000L else this

fun Long.toUnixTimeStamp() = this / 1000

fun Double.formatWithDot() = String.format(Locale.US, "%.5f", this)

fun Double.equalsValues(value: Double?) = "%.7f".format(this) == "%.7f".format(value)

fun <T> AbsDelegationAdapter<T>.setData(data: T) {
    items = data
    notifyDataSetChanged()
}