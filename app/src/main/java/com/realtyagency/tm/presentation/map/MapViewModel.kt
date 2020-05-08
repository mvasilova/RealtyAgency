package com.realtyagency.tm.presentation.map

import android.location.Location
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ResolvableApiException
import com.realtyagency.tm.app.platform.*
import com.realtyagency.tm.app.utils.MapLocationEvent
import org.greenrobot.eventbus.EventBus

class MapViewModel(private val locationProvider: LocationProvider) : BaseViewModel() {

    val location = MutableLiveData<Location>()
    val resolvableApiEvent = MutableLiveData<Event<ResolvableApiException>>()

    fun getLocation() {
        handleState(State.Loading)
        locationProvider.getLocation(
            {
                location.value = it
                Handler().postDelayed({ handleState(State.Loaded) }, 400)
            }, {
                handleState(State.Loaded)
                resolvableApiEvent.value = Event(it)
            }, false
        )
    }

    fun setLocation(latitude: Double, longitude: Double) {
        this.location.value?.latitude = latitude
        this.location.value?.longitude = longitude
    }

    fun sendEvent() {
        location.value?.let {
            EventBus.getDefault().postSticky(MapLocationEvent(location = it))
        }
    }

    fun navigateToNewRequest() {
        navigate(NavigationEvent.Exit)
    }

}