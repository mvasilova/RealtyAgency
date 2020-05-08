package com.realtyagency.tm.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.realtyagency.tm.app.extensions.fromJson

class RealtyPhotosConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromAttachments(photos: List<String>?): String? =
        gson.toJson(photos.orEmpty())

    @TypeConverter
    fun toAttachments(photos: String?) =
        gson.fromJson<List<String>?>(photos.orEmpty())
}