package com.realtyagency.tm.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NewRequest(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("number")
    val number: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("date")
    val date: Long? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("criticality")
    var criticality: String? = null,
    @SerializedName("typeName")
    var type: Int? = null,
    @SerializedName("text")
    var text: String? = null,
    @SerializedName("attachments")
    var attachments: List<Attachment>? = null,
    @SerializedName("responsible")
    val responsible: String? = null,
    @SerializedName("geotag")
    var geotag: Geotag? = null,
    var fromDB: Boolean? = false
) : Serializable