package com.realtyagency.tm.data.entities

import com.google.gson.annotations.SerializedName

data class RequestType(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null
)