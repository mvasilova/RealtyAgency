package com.realtyagency.tm.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class News(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("date")
    val date: Long? = null,
    @SerializedName("header")
    val header: String? = null,
    @SerializedName("content")
    val content: String? = null,
    @SerializedName("locale")
    val locale: String? = null,
    @SerializedName("seenByUser")
    var seenByUser: Boolean? = null,
    @SerializedName("attachments")
    val attachments: List<Attachment>? = null
) : Serializable