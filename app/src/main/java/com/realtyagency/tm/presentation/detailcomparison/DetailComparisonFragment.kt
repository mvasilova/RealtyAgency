package com.realtyagency.tm.presentation.detailcomparison

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.observe
import com.realtyagency.tm.app.platform.BaseFragment
import com.realtyagency.tm.data.db.entities.Comparison
import kotlinx.android.synthetic.main.fragment_detail_comparison.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailComparisonFragment : BaseFragment(R.layout.fragment_detail_comparison) {

    override val toolbarTitle: Any?
        get() = getString(R.string.placeholder_comparison_name, idComparison)

    override val screenViewModel by viewModel<DetailComparisonViewModel>() {
        parametersOf(idComparison)
    }

    private val idComparison by lazy { arguments?.getInt(DETAIL_COMPARISON) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupComparisons()

        observe(screenViewModel.comparisons, ::handleComparisons)

    }

    private fun handleComparisons(comparison: Comparison?) {
        tableComparison
    }

    private fun setupComparisons() {

    }

    companion object {
        private const val DETAIL_COMPARISON: String = "DetailRealty"
        fun newInstance(idComparison: Int) =
            DetailComparisonFragment().apply {
                arguments = bundleOf(DETAIL_COMPARISON to idComparison)
            }
    }
}