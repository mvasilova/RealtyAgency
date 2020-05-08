package com.realtyagency.tm.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Notification(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("date")
    val date: Long? = null,
    @SerializedName("seenByUser")
    var seenByUser: Boolean? = null
) : Serializable