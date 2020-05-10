package com.realtyagency.tm.presentation.comparison

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.dpToPx
import com.realtyagency.tm.app.extensions.getCompatColor
import com.realtyagency.tm.app.extensions.observe
import com.realtyagency.tm.app.platform.BaseFragment
import com.realtyagency.tm.app.platform.DIFF_CALLBACK
import com.realtyagency.tm.data.db.entities.Comparison
import com.realtyagency.tm.presentation.delegates.comparisonsDelegate
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ComparisonsFragment : BaseFragment(R.layout.fragment_list) {

    override val toolbarTitle: Any?
        get() = getString(R.string.title_comparisons)

    override val screenViewModel by viewModel<ComparisonsViewModel>()

    private val realtyAdapter by lazy {
        AsyncListDifferDelegationAdapter(
            DIFF_CALLBACK,
            comparisonsDelegate {
                screenViewModel.navigateToDetailComparison(it)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupComparisons()

        observe(screenViewModel.comparisons, ::handleComparisons)

    }

    private fun handleComparisons(list: List<Comparison>?) {
        realtyAdapter.items = list
    }

    private fun setupComparisons() {
        rvList.layoutManager = LinearLayoutManager(context)
        rvList.addItemDecoration(
            com.realtyagency.tm.presentation.common.DividerItemDecoration(
                requireContext().getCompatColor(R.color.colorLightView),
                1.dpToPx,
                16f.dpToPx
            )
        )
        rvList.adapter = realtyAdapter
    }
}