package com.realtyagency.tm.app.platform

import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.os.Looper
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

class LocationProvider(private val context: Context) {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback = LocationCallback()

    fun getLocation(
        locationListener: (Location) -> Unit,
        resolvableApiListener: (ResolvableApiException) -> Unit,
        observe: Boolean = false
    ) {

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
            interval = UPDATE_INTERVAL
            fastestInterval = FASTEST_INTERVAL
        }

        val locationSettingsRequest =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()

        LocationServices.getSettingsClient(context).checkLocationSettings(locationSettingsRequest)
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        //Gps не включен, показываем диалог
                        //exception.startResolutionForResult(this@MainActivity, REQUEST_CHECK_SETTINGS)
                        resolvableApiListener.invoke(exception)
                    } catch (ignore: IntentSender.SendIntentException) {
                    }
                }
            }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let {
                    locationListener.invoke(it.lastLocation)

                    if (!observe) {
                        fusedLocationClient.removeLocationUpdates(this)
                    }
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    fun clearAll() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        private const val UPDATE_INTERVAL = 15_000L
        private const val FASTEST_INTERVAL = 5_000L
    }
}