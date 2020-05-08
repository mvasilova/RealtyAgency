package com.realtyagency.tm.presentation.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.getCompatDrawable
import com.realtyagency.tm.app.extensions.observe
import com.realtyagency.tm.app.extensions.setData
import com.realtyagency.tm.app.platform.BaseFragment
import com.realtyagency.tm.presentation.delegates.categoryDelegate
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_progress.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    override val statusBarColor: Int
        get() = R.color.colorPrimary

    override val statusBarLightMode: Boolean
        get() = false

    override val screenViewModel by viewModel<HomeViewModel>()

    private val categoriesAdapter by lazy {
        ListDelegationAdapter(
            categoryDelegate {
                screenViewModel.navigateToListRealty(it)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressLayout?.background = requireActivity().getCompatDrawable(R.color.colorWhite)

        setupRequests()
        observe(screenViewModel.categories, ::handleCategories)
    }

    private fun handleCategories(list: List<String>?) {
        list?.let {
            val items = mutableListOf<String>().apply {
                addAll(it)
                add(getString(R.string.label_all_adverts))
            }
            categoriesAdapter.setData(items)
        }
    }

    private fun setupRequests() {
        rvCategories.layoutManager = LinearLayoutManager(context)
        rvCategories.adapter = categoriesAdapter
    }
}