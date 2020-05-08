package com.realtyagency.tm.domain.repository

import com.realtyagency.tm.app.platform.State
import com.realtyagency.tm.data.db.entities.Realty

interface RequestDraftRepository {

    suspend fun getRequestDraft(onSuccess: (Realty?) -> Unit, onState: (State) -> Unit)
    suspend fun setRequestDraft(realty: Realty, onSuccess: (Unit) -> Unit, onState: (State) -> Unit)
    suspend fun deleteRequestDraft(onSuccess: (Unit) -> Unit, onState: (State) -> Unit)
    suspend fun getCount(onSuccess: (Int) -> Unit, onState: (State) -> Unit)
}