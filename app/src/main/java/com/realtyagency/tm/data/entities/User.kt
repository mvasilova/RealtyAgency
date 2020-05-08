package com.realtyagency.tm.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("login")
    val login: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("domainInfo")
    val domainInfo: String? = null
) : Serializable