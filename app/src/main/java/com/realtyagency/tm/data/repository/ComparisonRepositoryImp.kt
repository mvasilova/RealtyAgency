package com.realtyagency.tm.data.repository

import com.realtyagency.tm.app.platform.BaseRepository
import com.realtyagency.tm.app.platform.ErrorHandler
import com.realtyagency.tm.app.platform.State
import com.realtyagency.tm.data.db.dao.ComparisonDao
import com.realtyagency.tm.data.db.entities.Comparison
import com.realtyagency.tm.data.db.entities.Realty
import com.realtyagency.tm.domain.repository.ComparisonRepository

class ComparisonRepositoryImp(
    errorHandler: ErrorHandler,
    private val comparisonDao: ComparisonDao
) :
    BaseRepository(errorHandler = errorHandler), ComparisonRepository {

    override fun getAllComparisons() = comparisonDao.getAll()

    override suspend fun insertComparison(
        comparison: Comparison,
        onSuccess: (Unit) -> Unit,
        onState: (State) -> Unit
    ) {
        execute(onSuccess = onSuccess, onState = onState) {
            comparisonDao.insertComparison(comparison)
        }
    }

    override suspend fun deleteComparison(
        comparisonId: Int,
        onSuccess: (Unit) -> Unit,
        onState: (State) -> Unit
    ) {
        execute(onSuccess = onSuccess, onState = onState) {
            comparisonDao.deleteComparison(comparisonId)
        }
    }

    override suspend fun addItemRealtyToList(
        comparisonId: Int,
        item: Realty,
        onSuccess: (Unit) -> Unit,
        onState: (State) -> Unit
    ) {
        execute(onSuccess = onSuccess, onState = onState) {
            comparisonDao.addItemRealtyToList(comparisonId, item)
        }
    }
}