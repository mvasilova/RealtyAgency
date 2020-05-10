package com.realtyagency.tm.app.di.module

import android.content.Context
import com.realtyagency.tm.data.db.RealtyAgencyDatabase
import org.koin.dsl.module

val roomModule = module {

    single { provideDatabase(get()) }
    single { getRealtyDao(get()) }
    single { getFavoriteDao(get()) }
    single { getComparisonDao(get()) }
}

fun provideDatabase(context: Context) = RealtyAgencyDatabase.buildDataSource(context)

fun getRealtyDao(realtyAgencyDatabase: RealtyAgencyDatabase) = realtyAgencyDatabase.realtyDao()

fun getFavoriteDao(realtyAgencyDatabase: RealtyAgencyDatabase) = realtyAgencyDatabase.favoriteDao()

fun getComparisonDao(realtyAgencyDatabase: RealtyAgencyDatabase) =
    realtyAgencyDatabase.comparisonDao()

