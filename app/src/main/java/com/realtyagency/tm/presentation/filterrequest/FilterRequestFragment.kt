package com.realtyagency.tm.presentation.filterrequest

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.getCompatDrawable
import com.realtyagency.tm.app.extensions.observe
import com.realtyagency.tm.app.extensions.refresh
import com.realtyagency.tm.app.extensions.setData
import com.realtyagency.tm.app.platform.BaseFragment
import com.realtyagency.tm.data.entities.FilterData
import com.realtyagency.tm.data.entities.RequestType
import com.realtyagency.tm.presentation.common.CustomStableIdAdapter
import com.realtyagency.tm.presentation.filterrequest.delegates.RequestTypeItems
import com.realtyagency.tm.presentation.filterrequest.delegates.filterRequestDelegate
import kotlinx.android.synthetic.main.fragment_filter_request.*
import kotlinx.android.synthetic.main.layout_progress.*
import kotlinx.android.synthetic.main.toolbar_with_icon.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FilterRequestFragment : BaseFragment(R.layout.fragment_filter_request) {

    override val toolbarTitle: Int?
        get() = R.string.toolbar_title_filter

    override val toolbarDrawableClose: Int?
        get() = R.drawable.ic_toolbar_close

    override val toolbarTextButtonConfirm: Int?
        get() = R.string.btn_ok

    private val viewModel: FilterRequestViewModel by viewModel {
        parametersOf(arguments?.getSerializable(FILTER_DATA))
    }

    override val screenViewModel by lazy { viewModel }

    private val filterRequestTypeAdapter by lazy {
        CustomStableIdAdapter(
            filterRequestDelegate {
                viewModel.changeTypeRequests(it)
            }
        )
    }

    private val filterRequestPriorityAdapter by lazy {
        CustomStableIdAdapter(
            filterRequestDelegate {
                viewModel.changePriorityRequests(it)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getRequestType()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressLayout?.background = requireActivity().getCompatDrawable(R.color.colorWhite)

        setupFilters()

        observe(viewModel.filters, ::handleFilters)
        observe(viewModel.requestType, ::handleTypes)

        tvButtonConfirm.setOnClickListener {
            viewModel.navigateToRequest()
        }

        rbFilterNewFirst.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                //viewModel.setOrderRequest(ApiRequest.ORDER.DESC)
            }
        }

        rbFilterOldFirst.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                //viewModel.setOrderRequest(ApiRequest.ORDER.ASC)
            }
        }

    }

    private fun handleTypes(requestTypes: List<RequestType>?) {
        progressLayout?.background = null
        requestTypes?.let {
            filterRequestTypeAdapter.setData(mutableListOf<RequestTypeItems>().apply {
                for (i in it.indices) {
                    add(
                        RequestTypeItems(
                            typeId = it[i].id.toString(),
                            requestTypeName = it[i].name.toString()
                        )
                    )
                }
            })
        }
        viewModel.filters.refresh()
    }

    private fun handleFilters(filterData: FilterData?) {

        filterData?.let {
            when (it.requestOrder) {
                //ApiRequest.ORDER.DESC -> rbFilterNewFirst.isChecked = true
                //ApiRequest.ORDER.ASC -> rbFilterOldFirst.isChecked = true
            }

            it.requestType?.let { list ->
                filterRequestTypeAdapter.items?.forEach { item ->
                    if (list.contains((item as RequestTypeItems).typeId.toInt())) {
                        item.checked = true
                        filterRequestTypeAdapter.notifyDataSetChanged()
                    } else {
                        item.checked = false
                        filterRequestTypeAdapter.notifyDataSetChanged()
                    }
                }
            }

            it.requestCriticality?.let { list ->
                filterRequestPriorityAdapter.items?.forEach { item ->
                    if (list.contains((item as RequestTypeItems).typeId)) {
                        item.checked = true
                        filterRequestPriorityAdapter.notifyDataSetChanged()
                    } else {
                        item.checked = false
                        filterRequestPriorityAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun setupFilters() {
        rvFilterRequestType.layoutManager = LinearLayoutManager(context)
        rvFilterRequestType.isNestedScrollingEnabled = false
        rvFilterRequestType.adapter = filterRequestTypeAdapter

        rvFilterRequestPriority.layoutManager = LinearLayoutManager(context)
        rvFilterRequestPriority.isNestedScrollingEnabled = false
        rvFilterRequestPriority.adapter = filterRequestPriorityAdapter

        filterRequestPriorityAdapter.setData(mutableListOf<RequestTypeItems>().apply {
            val list = resources.getStringArray(R.array.spinner_type_critical).asList()
            for (i in list.indices) {
//                add(
//                    RequestTypeItems(
//                        typeId = ApiRequest.CRITICALITY.values()[i].criticality,
//                        requestTypeName = list[i]
//                    )
//                )
            }
        })
    }

    companion object {

        const val FILTER_DATA = "FilterData"

        fun newInstance(filterData: FilterData): FilterRequestFragment =
            FilterRequestFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(FILTER_DATA, filterData)
                }
            }

    }
}
