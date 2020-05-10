package com.realtyagency.tm.domain.repository

import androidx.lifecycle.LiveData
import com.realtyagency.tm.app.platform.State
import com.realtyagency.tm.data.db.entities.Comparison
import com.realtyagency.tm.data.db.entities.Realty

interface ComparisonRepository {

    fun getAllComparisons(): LiveData<List<Comparison>>

    suspend fun insertComparison(
        comparison: Comparison,
        onSuccess: (Unit) -> Unit,
        onState: (State) -> Unit
    )

    suspend fun deleteComparison(
        comparisonId: Int,
        onSuccess: (Unit) -> Unit,
        onState: (State) -> Unit
    )

    suspend fun addItemRealtyToList(
        comparisonId: Int,
        item: Realty,
        onSuccess: (Unit) -> Unit,
        onState: (State) -> Unit
    )
}