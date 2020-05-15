package com.realtyagency.tm.domain.repository

import com.realtyagency.tm.app.platform.State
import com.realtyagency.tm.data.entities.FilterData

interface FilterRepository {

    suspend fun getAllFilters(
        category: String?,
        onSuccess: (FilterData) -> Unit,
        onState: (State) -> Unit
    )
}