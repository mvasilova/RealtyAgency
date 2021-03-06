package com.realtyagency.tm.data.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.realtyagency.tm.app.platform.DiffItem
import java.io.Serializable

@Entity(tableName = "realty")
data class Realty(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("lon")
    val lon: Double? = null,
    @SerializedName("typeAdvert")
    val typeAdvert: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @Embedded
    @SerializedName("parameters")
    val parameters: Parameters? = null,
    @SerializedName("photos")
    val photos: List<String>? = null,
    @SerializedName("premium")
    val premium: Boolean? = null,
    var isFavorite: Boolean? = null
) : Serializable, DiffItem {
    @Ignore
    override val itemId = id.toLong()
}