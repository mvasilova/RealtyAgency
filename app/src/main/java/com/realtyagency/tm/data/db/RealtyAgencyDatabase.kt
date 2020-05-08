package com.realtyagency.tm.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.realtyagency.tm.data.db.RealtyAgencyDatabase.Companion.DATABASE_VERSION
import com.realtyagency.tm.data.db.dao.FavoriteDao
import com.realtyagency.tm.data.db.dao.RealtyDao
import com.realtyagency.tm.data.db.entities.Favorite
import com.realtyagency.tm.data.db.entities.Realty

@Database(entities = [Realty::class, Favorite::class], version = DATABASE_VERSION)
abstract class RealtyAgencyDatabase : RoomDatabase() {
    abstract fun realtyDao(): RealtyDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "RealtyAgencyDatabase"
        fun buildDataSource(context: Context): RealtyAgencyDatabase =
            Room.databaseBuilder(context, RealtyAgencyDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}