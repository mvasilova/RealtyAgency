package com.realtyagency.tm.presentation.detailrealty

import androidx.lifecycle.MutableLiveData
import com.realtyagency.tm.app.platform.BaseViewModel
import com.realtyagency.tm.app.platform.NavigationEvent
import com.realtyagency.tm.data.db.entities.Comparison
import com.realtyagency.tm.data.db.entities.Realty
import com.realtyagency.tm.domain.repository.ComparisonRepository
import com.realtyagency.tm.domain.repository.FavoriteRepository
import com.realtyagency.tm.presentation.detailcomparison.DetailComparisonFragment
import com.realtyagency.tm.presentation.viewmedia.ViewMediaFilesFragment

class DetailRealtyViewModel(
    private val realty: Realty,
    private val favoriteRepository: FavoriteRepository,
    private val comparisonRepository: ComparisonRepository
) : BaseViewModel() {

    val comparisons = comparisonRepository.getAllComparisons()
    val isFavorite = favoriteRepository.observeRealtyFavorite(realty.id)
    val comparisonId = MutableLiveData<Int>()

    fun changeFavorite() {
        launch {
            favoriteRepository.actionFavorite(realty, {}, ::handleError)
        }
    }

    fun insertNewComparison() {
        launch {
            comparisonRepository.insertComparison(
                Comparison(date = System.currentTimeMillis(), realty = listOf(realty)),
                ::handleAddingComparison,
                ::handleError
            )
        }
    }

    fun addRealtyToListComparison(comparisonId: Int) {
        launch {
            comparisonRepository.addItemRealtyToList(
                comparisonId, realty,
                ::handleAddingComparison,
                ::handleError
            )
        }
    }

    private fun handleAddingComparison(comparisonId: Int) {
        this.comparisonId.value = comparisonId
    }

    private fun handleAddingComparison(comparisonId: Long) {
        this.comparisonId.value = comparisonId.toInt()
    }

    fun getComparisons() = comparisons.value

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

    fun navigateToDetailComparison(comparisonId: Int) {
        navigate(NavigationEvent.SwitchTab(1))
        navigate(NavigationEvent.PushFragment(DetailComparisonFragment.newInstance(comparisonId)))
    }

    fun onExit() {
        navigate(NavigationEvent.Exit)
    }
}