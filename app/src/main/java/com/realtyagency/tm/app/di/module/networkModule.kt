package com.realtyagency.tm.app.di.module

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.realtyagency.tm.BuildConfig
import com.realtyagency.tm.data.network.interceptor.HeaderInterceptor
import com.realtyagency.tm.data.network.interceptor.ValidationInterceptor
import com.realtyagency.tm.data.network.retrofit.ApiFirebase
import com.realtyagency.tm.data.storage.Pref
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val TIMEOUT: Long = 30

val networkModule = module {

    single { buildRetrofit(get(), get()) }

    single { buildOkHttp(get(), get()) }

    single { buildApiFirebase(get()) }

    single { buildJson() }

    single { provideHeaderInterceptor(get()) }

    single { provideValidationInterceptor(get()) }

}

private fun buildApiFirebase(retrofit: Retrofit): ApiFirebase? {
    return retrofit.create(ApiFirebase::class.java)
}

private fun buildJson() = GsonBuilder().create()


private fun buildOkHttp(
    headerInterceptor: HeaderInterceptor,
    validationInterceptor: ValidationInterceptor
): OkHttpClient {
    val okHttpClientBuilder = OkHttpClient.Builder()
    okHttpClientBuilder.connectTimeout(TIMEOUT, TimeUnit.SECONDS)
    okHttpClientBuilder.readTimeout(TIMEOUT, TimeUnit.SECONDS)
    okHttpClientBuilder.addInterceptor(headerInterceptor)
    okHttpClientBuilder.addInterceptor(validationInterceptor)

    if (BuildConfig.DEBUG) {
        //Ignore for sending newRequest (because duplicate query)
//        val loggingInterceptor =
//            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//        okHttpClientBuilder.addInterceptor(loggingInterceptor)
        okHttpClientBuilder.addNetworkInterceptor(StethoInterceptor())
    }

    return okHttpClientBuilder.build()
}

private fun buildRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_ENDPOINT)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

private fun provideHeaderInterceptor(pref: Pref) = HeaderInterceptor(pref)

private fun provideValidationInterceptor(gson: Gson) = ValidationInterceptor(gson)