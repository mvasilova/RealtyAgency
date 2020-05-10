package com.realtyagency.tm.app.di.module

import com.realtyagency.tm.data.repository.ComparisonRepositoryImp
import com.realtyagency.tm.data.repository.FavoriteRepositoryImp
import com.realtyagency.tm.data.repository.RealtyRepositoryImp
import com.realtyagency.tm.domain.repository.ComparisonRepository
import com.realtyagency.tm.domain.repository.FavoriteRepository
import com.realtyagency.tm.domain.repository.RealtyRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<RealtyRepository> { RealtyRepositoryImp(get(), get(), get()) }
    single<FavoriteRepository> { FavoriteRepositoryImp(get(), get()) }
    single<ComparisonRepository> { ComparisonRepositoryImp(get(), get()) }
}
