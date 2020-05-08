package com.realtyagency.tm.domain.repository

import com.realtyagency.tm.app.platform.State
import com.realtyagency.tm.data.entities.Request
import com.realtyagency.tm.data.entities.RequestType
import okhttp3.RequestBody

interface RequestRepository {

    suspend fun postRequest(
        requestBody: RequestBody,
        onSuccess: (Request) -> Unit,
        onState: (State) -> Unit
    )

    suspend fun getRequestList(
        order: String,
        type: List<Int>?,
        criticality: List<String>?,
        onSuccess: (List<Request>) -> Unit,
        onState: (State) -> Unit
    )

    suspend fun getAdditionalInformation(
        id: Int,
        onSuccess: (Request) -> Unit,
        onState: (State) -> Unit
    )

    suspend fun getCompleteRequests(
        order: String,
        type: List<Int>?,
        criticality: List<String>?,
        onSuccess: (Unit) -> Unit,
        onState: (State) -> Unit
    )

    suspend fun getRequestType(onSuccess: (List<RequestType>) -> Unit, onState: (State) -> Unit)
    suspend fun readRequest(id: Int, onSuccess: (Unit) -> Unit, onState: (State) -> Unit)
}