package com.realtyagency.tm.presentation.realty

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.observe
import com.realtyagency.tm.app.platform.BaseFragment
import com.realtyagency.tm.app.platform.DIFF_CALLBACK
import com.realtyagency.tm.app.utils.FilterRequestEvent
import com.realtyagency.tm.data.db.entities.Realty
import com.realtyagency.tm.presentation.common.CommonBottomDialogFragment
import com.realtyagency.tm.presentation.common.ItemDialog
import com.realtyagency.tm.presentation.delegates.realtyDelegate
import kotlinx.android.synthetic.main.fragment_requests.*
import kotlinx.android.synthetic.main.toolbar_with_icon.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class RealtyListFragment : BaseFragment(R.layout.fragment_requests),
    CommonBottomDialogFragment.BottomSheetDialogListener {

    override val toolbarTitle: Any?
        get() = arguments?.get(CATEGORY_TITLE)

    override val toolbarIconFilterVisible: Boolean
        get() = true

    override val toolbarDrawableClose: Int?
        get() = R.drawable.ic_toolbar_back

    override val screenViewModel by viewModel<RealtyListViewModel>() {
        parametersOf(
            if (arguments?.get(CATEGORY_TITLE) == getString(R.string.label_all_adverts)) {
                ""
            } else {
                arguments?.get(CATEGORY_TITLE)
            }
        )
    }

    private val realtyAdapter by lazy {
        AsyncListDifferDelegationAdapter(
            DIFF_CALLBACK,
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


//    private fun showBottomSheetDialog() {
//        val list = mutableListOf<ItemDialog>().apply {
//            add(ItemDialog(R.drawable.ic_request_new, R.string.label_new_request))
//            if (showDraft) add(ItemDialog(R.drawable.ic_request_edit, R.string.label_edit_request))
//        }
//
//        val dialog = CommonBottomDialogFragment.newInstance(list)
//
//        dialog.setTargetFragment(this, 0)
//        dialog.show(supportFragmentManager, TAG_REQUEST_DIALOG)
//    }

    override fun onItemDialogClick(itemDialog: ItemDialog) {
        when (itemDialog.title) {
            R.string.label_new_request -> {
                //viewModel.navigateToNewRequest(false)
            }

            R.string.label_edit_request -> {
                // viewModel.navigateToNewRequest(true)
            }
        }
    }

    private fun setupAdverts() {
        rvRequestsList.layoutManager = GridLayoutManager(context, 2)
        (rvRequestsList.layoutManager as? GridLayoutManager)?.spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if ((realtyAdapter.items[position] as? Realty)?.premium == true) {
                        2
                    } else {
                        1
                    }
                }
            }
        rvRequestsList.adapter = realtyAdapter
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onFilterRequestEvent(event: FilterRequestEvent) {
        rvRequestsList.scrollToPosition(0)
        screenViewModel.applyFilters(event.data)

        val stickyEvent = EventBus.getDefault().getStickyEvent(FilterRequestEvent::class.java)
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent)
        }
    }

    companion object {
        const val TAG_REQUEST_DIALOG = "RequestDialog"
        private const val CATEGORY_TITLE: String = "CategoryTitle"
        fun newInstance(category: String): RealtyListFragment =
            RealtyListFragment().apply {
                arguments = bundleOf(CATEGORY_TITLE to category)
            }
    }
}