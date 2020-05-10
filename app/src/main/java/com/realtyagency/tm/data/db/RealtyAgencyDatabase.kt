package com.realtyagency.tm.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.realtyagency.tm.data.db.RealtyAgencyDatabase.Companion.DATABASE_VERSION
import com.realtyagency.tm.data.db.converters.ComparisonRealtyConverter
import com.realtyagency.tm.data.db.converters.RealtyPhotosConverter
import com.realtyagency.tm.data.db.dao.ComparisonDao
import com.realtyagency.tm.data.db.dao.FavoriteDao
import com.realtyagency.tm.data.db.dao.RealtyDao
import com.realtyagency.tm.data.db.entities.Comparison
import com.realtyagency.tm.data.db.entities.Favorite
import com.realtyagency.tm.data.db.entities.Realty

@Database(
    entities = [Realty::class, Favorite::class, Comparison::class],
    version = DATABASE_VERSION
)
@TypeConverters(RealtyPhotosConverter::class, ComparisonRealtyConverter::class)
abstract class RealtyAgencyDatabase : RoomDatabase() {
    abstract fun realtyDao(): RealtyDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun comparisonDao(): ComparisonDao

    companion object {
        const val DATABASE_VERSION = 3
        private const val DATABASE_NAME = "RealtyAgencyDatabase"
        fun buildDataSource(context: Context): RealtyAgencyDatabase =
            Room.databaseBuilder(context, RealtyAgencyDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}