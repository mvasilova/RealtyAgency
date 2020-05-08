package com.realtyagency.tm.domain.repository

import com.realtyagency.tm.app.platform.State
import com.realtyagency.tm.data.db.entities.Realty
import com.realtyagency.tm.data.entities.response.FirebaseDataBaseResponse

interface RealtyRepository {

    suspend fun getFirebaseDataBase(
        onSuccess: (FirebaseDataBaseResponse) -> Unit,
        onState: (State) -> Unit
    )

    suspend fun getLocalDataBase(
        onSuccess: (List<Realty>) -> Unit,
        onState: (State) -> Unit
    )

    suspend fun getCountLocalDataBase(
        onSuccess: (Int) -> Unit,
        onState: (State) -> Unit
    )

    suspend fun setLocalDataBase(
        list: List<Realty>,
        onSuccess: (Unit) -> Unit,
        onState: (State) -> Unit
    )

    suspend fun getCategories(onSuccess: (List<String>) -> Unit, onState: (State) -> Unit)

    suspend fun getRealtyByCategory(
        category: String,
        onSuccess: (List<Realty>) -> Unit,
        onState: (State) -> Unit
    )
}