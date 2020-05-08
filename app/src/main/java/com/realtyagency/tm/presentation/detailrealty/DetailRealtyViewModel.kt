package com.realtyagency.tm.presentation.detailrealty

import com.realtyagency.tm.app.platform.BaseViewModel
import com.realtyagency.tm.app.platform.NavigationEvent
import com.realtyagency.tm.data.db.entities.Realty
import com.realtyagency.tm.domain.repository.FavoriteRepository
import com.realtyagency.tm.presentation.viewmedia.ViewMediaFilesFragment

class DetailRealtyViewModel(
    private val realty: Realty,
    private val favoriteRepository: FavoriteRepository
) : BaseViewModel() {

    val isFavorite = favoriteRepository.observeRealtyFavorite(realty.id)

    fun changeFavorite() {
        launch {
            favoriteRepository.actionFavorite(realty, {}, ::handleError)
        }
    }

    fun navigateToViewMediaFiles(url: String) {
        navigate(
            NavigationEvent.PushFragment(
                ViewMediaFilesFragment.newInstance(
                    url,
                    ArrayList(realty.photos.orEmpty())
                )
            )
        )
    }

    fun onExit() {
        navigate(NavigationEvent.Exit)
    }
}