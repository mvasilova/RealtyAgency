package com.realtyagency.tm.data.network.interceptor

import com.google.gson.Gson
import com.realtyagency.tm.app.utils.InvalidTokenEvent
import com.realtyagency.tm.data.entities.response.ErrorResponse
import okhttp3.Interceptor
import okhttp3.Response
import org.greenrobot.eventbus.EventBus

class ValidationInterceptor(private val gson: Gson) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val responseBody = response.peekBody(Long.MAX_VALUE)
        val content = responseBody.string()

        var errorResponse = ErrorResponse()
        if (response.code() != 200) {
            try {
                errorResponse = gson.fromJson(content, ErrorResponse::class.java)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        if (response.code() == 403 && errorResponse.error?.contains("AuthFailed") == false) {
            EventBus.getDefault().post(InvalidTokenEvent)
        }
        return response
    }
}