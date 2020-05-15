package com.realtyagency.tm.data.repository

import com.realtyagency.tm.app.platform.BaseRepository
import com.realtyagency.tm.app.platform.ErrorHandler
import com.realtyagency.tm.app.platform.State
import com.realtyagency.tm.data.db.dao.FilterDao
import com.realtyagency.tm.data.entities.FilterData
import com.realtyagency.tm.domain.repository.FilterRepository

class FilterRepositoryImp(
    errorHandler: ErrorHandler,
    private val filterDao: FilterDao
) : BaseRepository(errorHandler = errorHandler), FilterRepository {

    override suspend fun getAllFilters(
        category: String?,
        onSuccess: (FilterData) -> Unit,
        onState: (State) -> Unit
    ) {
        execute(onSuccess = onSuccess, onState = onState) {
            filterDao.getAllFilters(category)
        }
    }

}