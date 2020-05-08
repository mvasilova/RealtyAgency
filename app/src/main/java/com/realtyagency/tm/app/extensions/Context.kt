package com.realtyagency.tm.app.extensions

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat


fun Context.getCompatColor(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun Context.getCompatDrawable(@DrawableRes drawable: Int) =
    AppCompatResources.getDrawable(this, drawable)

val Context.screenHeight
    get() = resources.displayMetrics.heightPixels

val Context.screenWidth
    get() = resources.displayMetrics.widthPixels

fun Context.areNotificationsEnabled(channelId: String = "fcm_fallback_notification_channel"): Boolean {
    if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = manager.getNotificationChannel(channelId)
            return channel?.importance != NotificationManager.IMPORTANCE_NONE
        } else {
            true
        }
    }
    return false
}

fun Context.goToPhoneDial(phone: String = "89885438819") {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    startActivity(intent)
}
