package com.realtyagency.tm.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.realtyagency.tm.data.entities.FilterData

@Dao
interface FilterDao {

    @Query(
        "SELECT cost FROM realty WHERE CASE " +
                "WHEN :category is NULL THEN category NOT null " +
                "WHEN :category NOT NULL THEN category=:category END " +
                "ORDER BY cost ASC LIMIT 1"
    )
    fun getMinPrice(category: String?): Long

    @Query(
        "SELECT cost FROM realty WHERE CASE " +
                "WHEN :category is NULL THEN category NOT null " +
                "WHEN :category NOT NULL THEN category=:category END " +
                "ORDER BY cost DESC LIMIT 1"
    )
    fun getMaxPrice(category: String?): Long

    @Query(
        "SELECT DISTINCT typeHousing FROM realty WHERE CASE " +
                "WHEN :category is NULL THEN category NOT null " +
                "WHEN :category NOT NULL THEN category=:category END " +
                "ORDER BY typeHousing ASC"
    )
    fun getRealtyType(category: String?): List<String>

    @Query(
        "SELECT DISTINCT typeAdvert FROM realty WHERE CASE " +
                "WHEN :category is NULL THEN category NOT null " +
                "WHEN :category NOT NULL THEN category=:category END " +
                "ORDER BY typeAdvert ASC"
    )
    fun getAdvertType(category: String?): List<String>

    @Query(
        "SELECT DISTINCT repair FROM realty WHERE CASE " +
                "WHEN :category is NULL THEN category NOT null " +
                "WHEN :category NOT NULL THEN category=:category END " +
                "ORDER BY repair ASC"
    )
    fun getRealtyRepair(category: String?): List<String>

    @Transaction
    fun getAllFilters(category: String?) =
        FilterData(
            realtyCost = getMinPrice(category) to getMaxPrice(category),
            realtyType = getRealtyType(category),
            advertType = getAdvertType(category),
            realtyRepair = getRealtyRepair(category)
        )
}