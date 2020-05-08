package com.realtyagency.tm.data.repository

import androidx.lifecycle.map
import com.realtyagency.tm.app.platform.BaseRepository
import com.realtyagency.tm.app.platform.ErrorHandler
import com.realtyagency.tm.app.platform.State
import com.realtyagency.tm.data.db.dao.FavoriteDao
import com.realtyagency.tm.data.db.entities.Realty
import com.realtyagency.tm.domain.repository.FavoriteRepository

class FavoriteRepositoryImp(
    errorHandler: ErrorHandler,
    private val favoriteDao: FavoriteDao
) :
    BaseRepository(errorHandler = errorHandler), FavoriteRepository {

    override fun getAllFavorites() = favoriteDao.getAll()

    override fun observeRealtyFavorite(realtyId: Int) =
        favoriteDao.observeRealtyFavorite(realtyId).map { it > 0 }

    override suspend fun actionFavorite(
        realty: Realty,
        onSuccess: (Unit) -> Unit,
        onState: (State) -> Unit
    ) {
        execute(onSuccess = onSuccess, onState = onState) {
            favoriteDao.actionFavorite(realty)
        }
    }
}