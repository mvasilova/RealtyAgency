package com.realtyagency.tm.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.realtyagency.tm.data.db.entities.Realty

@Dao
interface RealtyDao {
    @Query("SELECT * FROM realty ORDER BY premium DESC")
    fun getAllRealty(): List<Realty>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setAllRealty(realty: List<Realty>)

    @Query("DELETE FROM realty")
    fun deleteRealty()

    @Query("SELECT COUNT(*) FROM realty")
    fun getCount(): Int

    @Query("SELECT DISTINCT category FROM realty ORDER BY category")
    fun getCategories(): List<String>

    @Query("SELECT * FROM realty WHERE category=:category ORDER BY premium DESC")
    fun getRealtyByCategory(category: String): List<Realty>
}