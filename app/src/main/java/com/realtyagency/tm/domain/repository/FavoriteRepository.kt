package com.realtyagency.tm.domain.repository

import androidx.lifecycle.LiveData
import com.realtyagency.tm.app.platform.State
import com.realtyagency.tm.data.db.entities.Realty

interface FavoriteRepository {

    fun getAllFavorites(): LiveData<List<Realty>>

    fun observeRealtyFavorite(realtyId: Int): LiveData<Boolean>

    suspend fun actionFavorite(realty: Realty, onSuccess: (Unit) -> Unit, onState: (State) -> Unit)
}