package com.realtyagency.tm.app.utils

import android.location.Location
import com.realtyagency.tm.data.entities.FilterData

object InvalidTokenEvent

object UpdateNotifications

object UpdateDotNotifications

data class FilterRequestEvent(val data: FilterData)

data class MapLocationEvent(val location: Location)