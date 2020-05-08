package com.realtyagency.tm.app.di.module

import android.content.Context
import com.google.gson.Gson
import com.realtyagency.tm.app.platform.ErrorHandler
import com.realtyagency.tm.app.platform.LocationProvider
import com.realtyagency.tm.app.platform.NetworkHandler
import com.realtyagency.tm.app.platform.ResourceManager
import org.koin.dsl.module

val appModule = module {

    single { provideNetworkHandler(get()) }

    single { provideErrorHandler(get()) }

    single { provideResourceManager(get()) }

    factory { provideLocationProvider(get()) }
}

fun provideNetworkHandler(context: Context): NetworkHandler {
    return NetworkHandler(context)
}

fun provideErrorHandler(networkHandler: NetworkHandler): ErrorHandler {
    return ErrorHandler(networkHandler, Gson())
}

fun provideResourceManager(context: Context): ResourceManager {
    return ResourceManager(context)
}

fun provideLocationProvider(context: Context): LocationProvider {
    return LocationProvider(context)
}