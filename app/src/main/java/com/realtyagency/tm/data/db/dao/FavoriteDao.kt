package com.realtyagency.tm.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.realtyagency.tm.data.db.entities.Realty

@Dao
interface FavoriteDao {
    @Query("SELECT realty.* FROM realty INNER JOIN favorite ON id = idFavorite ORDER BY typeAdvert, premium DESC")
    fun getAll(): LiveData<List<Realty>>

    @Query("INSERT INTO favorite (idFavorite) VALUES (:id)")
    fun insertRealty(id: Int)

    @Query("DELETE FROM favorite WHERE idFavorite = :realtyId")
    fun deleteRealty(realtyId: Int)

    @Query("SELECT COUNT(idFavorite) FROM favorite WHERE idFavorite = :realtyId")
    fun observeRealtyFavorite(realtyId: Int): LiveData<Int>

    @Query("SELECT COUNT(idFavorite) FROM favorite WHERE idFavorite = :realtyId")
    fun isDrinkFavorite(realtyId: Int): Int

    @Transaction
    fun actionFavorite(realty: Realty) {
        if (isDrinkFavorite(realty.id) > 0) {
            deleteRealty(realty.id)
        } else {
            insertRealty(realty.id)
        }
    }

}