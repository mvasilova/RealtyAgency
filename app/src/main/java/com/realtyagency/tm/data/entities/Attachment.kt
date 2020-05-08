package com.realtyagency.tm.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Attachment(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("path")
    val path: String? = null,
    var pathFile: String? = null,
    var pathCacheFile: String? = null
) : Serializable