package com.realtyagency.tm.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.realtyagency.tm.data.db.entities.Comparison
import com.realtyagency.tm.data.db.entities.Realty

@Dao
interface ComparisonDao {
    @Query("SELECT * FROM comparison ORDER BY idComparison")
    fun getAll(): LiveData<List<Comparison>>

    @Query("SELECT * FROM comparison WHERE idComparison=:comparisonId")
    fun getComparisonById(comparisonId: Int): Comparison

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComparison(comparison: Comparison)

    @Query("DELETE FROM comparison WHERE idComparison = :comparisonId")
    fun deleteComparison(comparisonId: Int)

    @Query("UPDATE comparison SET realty=:list WHERE idComparison = :comparisonId")
    fun updateRealtyOnList(comparisonId: Int, list: List<Realty>)

    @Query("SELECT COUNT() FROM comparison")
    fun getCountComparisons(): Int

    @Transaction
    fun addItemRealtyToList(comparisonId: Int, item: Realty) {
        val list = getComparisonById(comparisonId).realty.toMutableList()
        list.add(item)
        updateRealtyOnList(comparisonId, list)
    }

}