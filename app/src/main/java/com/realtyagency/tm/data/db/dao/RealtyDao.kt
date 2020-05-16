package com.realtyagency.tm.data.db.dao

import androidx.room.*
import com.realtyagency.tm.data.db.entities.Realty
import com.realtyagency.tm.data.entities.FilterData
import com.realtyagency.tm.data.enums.FilterSort

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

    @Query(
        "SELECT * FROM realty WHERE CASE " +
                "WHEN :category is NULL THEN category NOT null " +
                "WHEN :category NOT NULL THEN category=:category END " +
                "ORDER BY premium DESC"
    )
    fun getRealtyByCategory(category: String?): List<Realty>

    @Transaction
    fun getRealtyByFilters(category: String?, filters: FilterData): List<Realty> {
        var list = getRealtyByCategory(category)
        list = when (filters.realtySort) {
            FilterSort.DATE_ASC -> list.sortedBy { it.parameters?.datePublic }
            FilterSort.DATE_DESC -> list.sortedByDescending { it.parameters?.datePublic }
            FilterSort.COST_ASC -> list.sortedBy { it.parameters?.cost }
            FilterSort.COST_DESC -> list.sortedByDescending { it.parameters?.cost }
            else -> list
        }

        if (filters.realtyCost != null && filters.realtyCost?.first != null && filters.realtyCost?.second != null) {
            list = list.filter {
                it.parameters?.cost in filters.realtyCost?.first!!..filters.realtyCost?.second!!
            }
        }

        if (filters.realtyTypeSell != null) {
            if (filters.realtyTypeSell?.first == null || filters.realtyTypeSell?.first == false && filters.realtyTypeSell?.second == true) {
                list = list.filter {
                    it.parameters?.isRent == true
                }
            } else if (filters.realtyTypeSell?.first == true && (filters.realtyTypeSell?.second == false || filters.realtyTypeSell?.second == null)) {
                list = list.filter {
                    it.parameters?.isRent == false || it.parameters?.isRent == null
                }
            }
        }

        if (filters.advertType?.isNullOrEmpty() == false) {
            list = list.filter { realty ->
                filters.advertType?.any { it == realty.typeAdvert } == true
            }
        }

        if (filters.realtyType?.isNullOrEmpty() == false) {
            list = list.filter { realty ->
                filters.realtyType?.any { it == realty.parameters?.typeHousing } == true
            }
        }

        if (filters.realtyRepair?.isNullOrEmpty() == false) {
            list = list.filter { realty ->
                filters.realtyRepair?.any { it == realty.parameters?.repair } == true
            }
        }

        return list
    }
}