package com.realtyagency.tm.data.db.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Parameters(
    @SerializedName("cost")
    val cost: Long? = null,
    @SerializedName("countFloor")
    val countFloor: Int? = null,
    @SerializedName("countRoom")
    val countRoom: Int? = null,
    @SerializedName("datePublic")
    val datePublic: Long? = null,
    @SerializedName("distanceFromCenter")
    val distanceFromCenter: Double? = null,
    @SerializedName("floorFlat")
    val floorFlat: Int? = null,
    @SerializedName("isRent")
    val isRent: Boolean? = null,
    @SerializedName("liftEnabled")
    val liftEnabled: String? = null,
    @SerializedName("repair")
    val repair: String? = null,
    @SerializedName("squareHome")
    val squareHome: Double? = null,
    @SerializedName("squarePlot")
    val squarePlot: Double? = null,
    @SerializedName("typeHousing")
    val typeHousing: String? = null,
    @SerializedName("yearOfConstruction")
    val yearOfConstruction: Int? = null
) : Serializable