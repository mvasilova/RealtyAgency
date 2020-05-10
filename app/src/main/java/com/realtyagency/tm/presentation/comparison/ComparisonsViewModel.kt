package com.realtyagency.tm.presentation.comparison

import com.realtyagency.tm.app.platform.BaseViewModel
import com.realtyagency.tm.domain.repository.ComparisonRepository

class ComparisonsViewModel(
    private val comparisonRepository: ComparisonRepository
) : BaseViewModel() {

    val comparisons = comparisonRepository.getAllComparisons()

    fun navigateToDetailComparison(comparisonId: Int) {
        //navigate(NavigationEvent.PushFragment(DetailRealtyFragment.newInstance(realty)))
    }
}