package com.realtyagency.tm.presentation.comparison

import com.realtyagency.tm.app.platform.BaseViewModel
import com.realtyagency.tm.app.platform.NavigationEvent
import com.realtyagency.tm.domain.repository.ComparisonRepository
import com.realtyagency.tm.presentation.detailcomparison.DetailComparisonFragment

class ComparisonsViewModel(
    private val comparisonRepository: ComparisonRepository
) : BaseViewModel() {

    val comparisons = comparisonRepository.getAllComparisons()

    fun deleteComparison(comparisonId: Int) {
        launch {
            comparisonRepository.deleteComparison(comparisonId, {}, ::handleError)
        }
    }

    fun navigateToDetailComparison(comparisonId: Int) {
        navigate(NavigationEvent.PushFragment(DetailComparisonFragment.newInstance(comparisonId)))
    }
}