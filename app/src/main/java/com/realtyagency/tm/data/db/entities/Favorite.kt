package com.realtyagency.tm.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite")
data class Favorite(
    @PrimaryKey
    val idFavorite: Int
) : Serializable