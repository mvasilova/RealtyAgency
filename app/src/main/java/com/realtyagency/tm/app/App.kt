package com.realtyagency.tm.app

import android.app.Application
import com.facebook.stetho.Stetho
import com.google.android.gms.maps.MapsInitializer
import com.realtyagency.tm.BuildConfig
import com.realtyagency.tm.app.di.module.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        //MapsInitializer.initialize(this)

        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(
                listOf(
                    appModule,
                    networkModule,
                    repositoryModule,
                    storageModule,
                    viewModelModule,
                    roomModule
                )
            )
        }

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}