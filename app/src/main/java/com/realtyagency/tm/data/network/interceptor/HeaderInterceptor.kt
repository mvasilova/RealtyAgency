package com.realtyagency.tm.data.network.interceptor

import com.realtyagency.tm.data.storage.Pref
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(
    private val pref: Pref
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        when {
            !pref.authToken.isNullOrEmpty() -> {
                val newRequest =
                    chain.request().newBuilder()
                        .header("X-AUTH-TOKEN", pref.authToken.toString()).build()

                return chain.proceed(newRequest)
            }
        }

        return chain.proceed(chain.request())
    }
}