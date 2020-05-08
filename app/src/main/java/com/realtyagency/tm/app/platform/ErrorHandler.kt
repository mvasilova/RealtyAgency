package com.realtyagency.tm.app.platform

import com.google.gson.Gson
import com.realtyagency.tm.app.platform.Failure.*
import com.realtyagency.tm.data.entities.response.ErrorResponse
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.util.concurrent.CancellationException

class ErrorHandler(
    private val networkHandler: NetworkHandler,
    private val gson: Gson
) {

    fun proceedException(exception: Throwable): Failure {
        when {
            withoutNetworkConnection() -> {
                return NetworkConnection
            }

            exception is HttpException -> {
                try {
                    val error = gson.fromJson(
                        exception.response()?.errorBody()?.string(),
                        ErrorResponse::class.java
                    )
                    return when (error.error) {
                        "UnknownError" -> UnknownError
                        else -> CommonError
                    }
                } catch (e: Exception) {
                    ServerError
                }
            }

            exception is SocketTimeoutException -> {
                return ServerError
            }

            exception is CancellationException -> {
                return CommonError
            }

            exception is KotlinNullPointerException -> {
                return CommonError
            }

            else -> CommonError
        }

        return CommonError
    }

    fun withoutNetworkConnection() = !networkHandler.isConnected
}