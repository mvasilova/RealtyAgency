package com.realtyagency.tm.app.extensions

import android.content.Context
import android.content.res.Resources
import com.hannesdorfmann.adapterdelegates4.AbsDelegationAdapter
import com.realtyagency.tm.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.dpToPx: Float
    get() = (this * Resources.getSystem().displayMetrics.density)


fun Long.toDateFormatGmt(
    context: Context,
    pattern: String = context.getString(R.string.date_pattern_dmy)
): String =
    SimpleDateFormat(pattern, Locale.getDefault())
        .apply { timeZone = TimeZone.getTimeZone("GMT") }
        .format(this.checkUnixTimeStamp())

fun Long.checkUnixTimeStamp() = if (this.toString().length <= 10) this * 1000L else this

fun Long.toUnixTimeStamp() = this / 1000

fun Double.formatWithDot() = String.format(Locale.US, "%.5f", this)

fun Double.equalsValues(value: Double?) = "%.7f".format(this) == "%.7f".format(value)

fun Int?.toStringOrNotData(): String = this?.toString() ?: "н/д"

fun Double?.toStringOrNotData(): String = this?.toString() ?: "н/д"

fun String?.toStringOrNotData(): String = this ?: "н/д"

fun Long?.formatToCurrency(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("ru","RU"))
    format.maximumFractionDigits = 0
    return format.format(this ?: 0)
}

fun <T> AbsDelegationAdapter<T>.setData(data: T) {
    items = data
    notifyDataSetChanged()
}

fun <T : Any> List<T?>.ifNotNullOrEmpty(action: (List<T>) -> Unit) {
    val list = filterNotNull()
    if (!list.isNullOrEmpty()) {
        action.invoke(list)
    }
}