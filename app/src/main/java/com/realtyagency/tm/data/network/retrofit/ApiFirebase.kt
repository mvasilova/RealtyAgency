package com.realtyagency.tm.data.network.retrofit

import com.realtyagency.tm.data.entities.response.FirebaseDataBaseResponse
import retrofit2.http.GET

interface ApiFirebase {

    @GET(PATH_DATABASE)
    suspend fun getDataBase(): FirebaseDataBaseResponse

    companion object {
        const val PATH_DATABASE = ".json"
    }
}