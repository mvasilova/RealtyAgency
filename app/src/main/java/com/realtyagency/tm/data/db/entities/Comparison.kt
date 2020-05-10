package com.realtyagency.tm.data.db.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.realtyagency.tm.app.platform.DiffItem
import java.io.Serializable

@Entity(tableName = "comparison")
data class Comparison(
    @PrimaryKey(autoGenerate = true)
    val idComparison: Int = 0,
    val realty: List<Realty>
) : Serializable, DiffItem {
    @Ignore
    override val itemId = idComparison.toLong()
}