package com.realtyagency.tm.presentation.realty

import androidx.lifecycle.MutableLiveData
import com.realtyagency.tm.app.platform.BaseViewModel
import com.realtyagency.tm.app.platform.NavigationEvent
import com.realtyagency.tm.data.db.entities.Realty
import com.realtyagency.tm.data.entities.FilterData
import com.realtyagency.tm.domain.repository.FavoriteRepository
import com.realtyagency.tm.domain.repository.RealtyRepository
import com.realtyagency.tm.presentation.detailrealty.DetailRealtyFragment
import com.realtyagency.tm.presentation.filter.FilterFragment
import com.snakydesign.livedataextensions.combineLatest

class RealtyListViewModel(
    private val category: String?,
    private val realtyRepository: RealtyRepository,
    private val favoriteRepository: FavoriteRepository
) : BaseViewModel() {

    private val adverts = MutableLiveData<List<Realty>>()
    private val favorites = favoriteRepository.getAllFavorites()
    val combineList =
        combineLatest(adverts, favorites) { adverts: List<Realty>, favorites: List<Realty> ->
            updateItems(adverts, favorites)
        }

    private var filters = FilterData()

    init {
        getAdverts()
    }

    private fun getAdverts() {
        launch {
            realtyRepository.getRealtyByFilters(
                category,
                filters,
                onSuccess = ::handleRealty,
                onState = ::handleState
            )
        }
    }

    fun changeFavorite(realty: Realty) {
        launch {
            favoriteRepository.actionFavorite(realty, {}, ::handleError)
        }
    }

    private fun handleRealty(list: List<Realty>) {
        adverts.value = list
    }

    private fun updateItems(adverts: List<Realty>?, favorites: List<Realty>?): List<Realty>? {
        val list = adverts?.map { it.copy() }
        list?.forEach { advert ->
            advert.isFavorite = favorites?.find { it.id == advert.id } != null
        }
        return list
    }

    fun applyFilters(data: FilterData) {
        filters = data
        getAdverts()
    }

    fun navigateToDetailRealty(realty: Realty) {
        navigate(NavigationEvent.PushFragment(DetailRealtyFragment.newInstance(realty)))
    }

    fun navigateToFilterRequest() {
        navigate(
            NavigationEvent.PushFragment(
                FilterFragment.newInstance(
                    category,
                    filters
                )
            )
        )
    }
}