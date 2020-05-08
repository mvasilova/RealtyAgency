package com.realtyagency.tm.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Geotag(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("latitude")
    val latitude: Float? = null,
    @SerializedName("longitude")
    val longitude: Float? = null
) : Serializable