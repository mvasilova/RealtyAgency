package com.realtyagency.tm.presentation.favorite

import com.realtyagency.tm.app.platform.BaseViewModel
import com.realtyagency.tm.app.platform.NavigationEvent
import com.realtyagency.tm.data.db.entities.Realty
import com.realtyagency.tm.domain.repository.FavoriteRepository
import com.realtyagency.tm.presentation.detailrealty.DetailRealtyFragment

class FavoritesViewModel(
    private val favoriteRepository: FavoriteRepository
) : BaseViewModel() {

    val favorites = favoriteRepository.getAllFavorites()

    fun changeFavorite(realty: Realty) {
        launch {
            favoriteRepository.actionFavorite(realty, {}, ::handleError)
        }
    }

    fun navigateToDetailRealty(realty: Realty) {
        navigate(NavigationEvent.PushFragment(DetailRealtyFragment.newInstance(realty)))
    }
}