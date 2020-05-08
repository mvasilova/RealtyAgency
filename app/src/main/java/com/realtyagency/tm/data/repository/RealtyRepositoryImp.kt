package com.realtyagency.tm.data.repository

import com.realtyagency.tm.app.platform.BaseRepository
import com.realtyagency.tm.app.platform.ErrorHandler
import com.realtyagency.tm.app.platform.State
import com.realtyagency.tm.data.db.dao.RealtyDao
import com.realtyagency.tm.data.db.entities.Realty
import com.realtyagency.tm.data.entities.response.FirebaseDataBaseResponse
import com.realtyagency.tm.data.network.retrofit.ApiFirebase
import com.realtyagency.tm.domain.repository.RealtyRepository

class RealtyRepositoryImp(
    errorHandler: ErrorHandler,
    private val apiFirebase: ApiFirebase,
    private val realtyDao: RealtyDao
) : BaseRepository(errorHandler = errorHandler), RealtyRepository {

    override suspend fun getFirebaseDataBase(
        onSuccess: (FirebaseDataBaseResponse) -> Unit,
        onState: (State) -> Unit
    ) {
        execute(onSuccess = onSuccess, onState = onState) {
            apiFirebase.getDataBase()
        }
    }


    override suspend fun getLocalDataBase(
        onSuccess: (List<Realty>) -> Unit,
        onState: (State) -> Unit
    ) {
        execute(onSuccess = onSuccess, onState = onState) {
            realtyDao.getAllRealty()
        }
    }

    override suspend fun getCountLocalDataBase(onSuccess: (Int) -> Unit, onState: (State) -> Unit) {
        execute(onSuccess = onSuccess, onState = onState) {
            realtyDao.getCount()
        }
    }

    override suspend fun setLocalDataBase(
        list: List<Realty>,
        onSuccess: (Unit) -> Unit,
        onState: (State) -> Unit
    ) {
        execute(onSuccess = onSuccess, onState = onState) {
            realtyDao.setAllRealty(list)
        }
    }

    override suspend fun getCategories(
        onSuccess: (List<String>) -> Unit,
        onState: (State) -> Unit
    ) {
        execute(onSuccess = onSuccess, onState = onState) {
            realtyDao.getCategories()
        }
    }

    override suspend fun getRealtyByCategory(
        category: String,
        onSuccess: (List<Realty>) -> Unit,
        onState: (State) -> Unit
    ) {
        execute(onSuccess = onSuccess, onState = onState) {
            realtyDao.getRealtyByCategory(category)
        }
    }

}