package com.realtyagency.tm.app.platform

import android.content.Context
import android.net.ConnectivityManager

/**
 * Injectable class which returns information about the network connection state.
 */
class NetworkHandler(private val context: Context) {

    val isConnected get() = checkInternet()

    private fun checkInternet(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        var result = false
        if (connectivityManager != null) {
            result = connectivityManager.activeNetwork != null
        }
        return result
    }
}