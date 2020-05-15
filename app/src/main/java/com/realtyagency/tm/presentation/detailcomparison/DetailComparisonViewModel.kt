package com.realtyagency.tm.presentation.detailcomparison

import com.realtyagency.tm.app.platform.BaseViewModel
import com.realtyagency.tm.data.db.entities.Realty
import com.realtyagency.tm.domain.repository.ComparisonRepository

class DetailComparisonViewModel(
    private val comparisonId: Int,
    private val comparisonRepository: ComparisonRepository
) : BaseViewModel() {

    val comparisons = comparisonRepository.getComparison(comparisonId)

    fun removeItemRealtyToList(realty: Realty) {
        launch {
            if (comparisons.value?.realty?.size ?: 0 > 1) {
                comparisonRepository.removeItemRealtyToList(comparisonId, realty, {}, ::handleError)
            }
        }
    }
}