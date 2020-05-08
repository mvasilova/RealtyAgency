package com.realtyagency.tm.app.di.module

import android.content.Context
import com.realtyagency.tm.data.storage.Pref
import org.koin.dsl.module

val storageModule = module {

    single { provideSharedPreferences(get()) }
}

fun provideSharedPreferences(context: Context): Pref {
    return Pref(context)
}