package com.realtyagency.tm.presentation.favorite

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.observe
import com.realtyagency.tm.app.platform.BaseFragment
import com.realtyagency.tm.app.platform.DiffCallback
import com.realtyagency.tm.app.platform.DiffItem
import com.realtyagency.tm.data.db.entities.Realty
import com.realtyagency.tm.presentation.delegates.Category
import com.realtyagency.tm.presentation.delegates.favoriteCategoryDelegate
import com.realtyagency.tm.presentation.delegates.realtyDelegate
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : BaseFragment(R.layout.fragment_list) {

    override val toolbarTitle: Any?
        get() = getString(R.string.title_favorites)

    override val screenViewModel by viewModel<FavoritesViewModel>()

    private val realtyAdapter by lazy {
        AsyncListDifferDelegationAdapter(
            DiffCallback,
            realtyDelegate({
                screenViewModel.navigateToDetailRealty(it)
            }, {
                screenViewModel.changeFavorite(it)
            }),
            favoriteCategoryDelegate()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdverts()

        observe(screenViewModel.favorites, ::handleAdverts)

    }

    private fun handleAdverts(list: List<Realty>?) {
        list?.forEach { it.isFavorite = true }
        val group = list?.groupBy { it.category }
        val items = mutableListOf<DiffItem>().apply {
            group?.forEach {
                add(Category(it.key.orEmpty()))
                addAll(it.value)
            }
        }
        realtyAdapter.items = items
        tvListEmpty.isVisible = items.isEmpty()
    }

    private fun setupAdverts() {
        rvList.layoutManager = GridLayoutManager(context, 2)
        (rvList.layoutManager as? GridLayoutManager)?.spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when {
                        (realtyAdapter.items[position] as? Realty)?.premium == true -> {
                            2
                        }
                        realtyAdapter.items[position] is Category -> {
                            2
                        }
                        else -> {
                            1
                        }
                    }
                }
            }
        rvList.adapter = realtyAdapter
    }
}