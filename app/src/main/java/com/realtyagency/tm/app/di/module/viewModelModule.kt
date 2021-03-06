package com.realtyagency.tm.app.di.module

import com.realtyagency.tm.data.db.entities.Realty
import com.realtyagency.tm.data.entities.FilterData
import com.realtyagency.tm.presentation.SplashViewModel
import com.realtyagency.tm.presentation.comparison.ComparisonsViewModel
import com.realtyagency.tm.presentation.detailcomparison.DetailComparisonViewModel
import com.realtyagency.tm.presentation.detailrealty.DetailRealtyViewModel
import com.realtyagency.tm.presentation.favorite.FavoritesViewModel
import com.realtyagency.tm.presentation.filter.FilterViewModel
import com.realtyagency.tm.presentation.home.HomeViewModel
import com.realtyagency.tm.presentation.main.MainViewModel
import com.realtyagency.tm.presentation.map.MapViewModel
import com.realtyagency.tm.presentation.realty.RealtyListViewModel
import com.realtyagency.tm.presentation.viewmedia.ViewMediaFilesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (realty: Realty) -> DetailRealtyViewModel(realty, get(), get()) }
    viewModel { (comparisonId: Int) -> DetailComparisonViewModel(comparisonId, get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { (category: String) -> RealtyListViewModel(category, get(), get()) }
    viewModel { (category: String, filterData: FilterData) ->
        FilterViewModel(
            category,
            filterData,
            get()
        )
    }
    viewModel { FavoritesViewModel(get()) }
    viewModel { MapViewModel(get()) }
    viewModel { ViewMediaFilesViewModel() }
    viewModel { ComparisonsViewModel(get()) }
}