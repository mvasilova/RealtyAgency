package com.realtyagency.tm.data.entities.response


import com.google.gson.annotations.SerializedName
import com.realtyagency.tm.data.db.entities.Realty

data class FirebaseDataBaseResponse(
    @SerializedName("realty")
    val realty: List<Realty>? = null
)