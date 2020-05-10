package com.realtyagency.tm.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.realtyagency.tm.app.extensions.fromJson
import com.realtyagency.tm.data.db.entities.Realty

class ComparisonRealtyConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromRealty(list: List<Realty>?): String? =
        gson.toJson(list.orEmpty())

    @TypeConverter
    fun toRealty(item: String?) =
        gson.fromJson<List<Realty>?>(item.toString())
}