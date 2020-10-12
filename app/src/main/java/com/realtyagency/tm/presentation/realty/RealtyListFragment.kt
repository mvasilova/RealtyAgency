package com.realtyagency.tm.presentation.realty

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.observe
import com.realtyagency.tm.app.platform.BaseFragment
import com.realtyagency.tm.app.platform.DiffCallback
import com.realtyagency.tm.app.utils.FilterRequestEvent
import com.realtyagency.tm.data.db.entities.Realty
import com.realtyagency.tm.presentation.delegates.realtyDelegate
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.toolbar_with_icon.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class RealtyListFragment : BaseFragment(R.layout.fragment_list) {

    override val toolbarTitle: Any?
        get() = arguments?.get(CATEGORY_TITLE)

    override val toolbarIconFilterVisible: Boolean
        get() = true

    override val toolbarDrawableClose: Int?
        get() = R.drawable.ic_toolbar_back

    override val screenViewModel by viewModel<RealtyListViewModel> {
        parametersOf(
            if (arguments?.get(CATEGORY_TITLE) == getString(R.string.label_all_adverts)) {
                null
            } else {
                arguments?.get(CATEGORY_TITLE)
            }
        )
    }

    private val realtyAdapter by lazy {
        AsyncListDifferDelegationAdapter(
            DiffCallback,
            realtyDelegate({
                screenViewModel.navigateToDetailRealty(it)
            }, {
                screenViewModel.changeFavorite(it)
            })
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdverts()

        ivFilterRequests.setOnClickListener {
            screenViewModel.navigateToFilterRequest()
        }

        observe(screenViewModel.combineList, ::handleAdverts)

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    private fun handleAdverts(list: List<Realty>?) {
        realtyAdapter.items = list
    }

    private fun setupAdverts() {
        rvList.layoutManager = GridLayoutManager(context, 2)
        (rvList.layoutManager as? GridLayoutManager)?.spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if ((realtyAdapter.items[position] as? Realty)?.premium == true) {
                        2
                    } else {
                        1
                    }
                }
            }
        rvList.adapter = realtyAdapter
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onFilterRequestEvent(event: FilterRequestEvent) {
        screenViewModel.applyFilters(event.data)
        Handler().postDelayed({
            rvList.smoothScrollToPosition(0)
        }, 200)

        val stickyEvent = EventBus.getDefault().getStickyEvent(FilterRequestEvent::class.java)
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent)
        }
    }

    companion object {
        private const val CATEGORY_TITLE: String = "CategoryTitle"
        fun newInstance(category: String): RealtyListFragment =
            RealtyListFragment().apply {
                arguments = bundleOf(CATEGORY_TITLE to category)
            }
    }
}