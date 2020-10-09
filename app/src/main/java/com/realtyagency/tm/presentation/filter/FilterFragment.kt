package com.realtyagency.tm.presentation.filter

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.slider.RangeSlider
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.*
import com.realtyagency.tm.app.platform.BaseFragment
import com.realtyagency.tm.app.platform.DisplayableItem
import com.realtyagency.tm.data.entities.FilterData
import com.realtyagency.tm.data.enums.FilterSort
import com.realtyagency.tm.presentation.common.CustomStableIdAdapter
import com.realtyagency.tm.presentation.filter.delegates.FilterTitle
import com.realtyagency.tm.presentation.filter.delegates.RealtyTypeItems
import com.realtyagency.tm.presentation.filter.delegates.filterRequestDelegate
import com.realtyagency.tm.presentation.filter.delegates.filterTitleDelegate
import kotlinx.android.synthetic.main.fragment_filter_request.*
import kotlinx.android.synthetic.main.layout_progress.*
import kotlinx.android.synthetic.main.toolbar_with_icon.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

class FilterFragment : BaseFragment(R.layout.fragment_filter_request) {

    override val toolbarTitle: Int?
        get() = R.string.toolbar_title_filter

    override val toolbarDrawableClose: Int?
        get() = R.drawable.ic_toolbar_close

    override val toolbarTextButtonConfirm: Int?
        get() = R.string.btn_ok

    override val screenViewModel by viewModel<FilterViewModel> {
        parametersOf(arguments?.getString(FILTER_CATEGORY), arguments?.getSerializable(FILTER_DATA))
    }

    private val filterAdvertAdapter by lazy {
        CustomStableIdAdapter(
            filterTitleDelegate(),
            filterRequestDelegate {
                screenViewModel.changeFilterTypes(it)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressLayout?.background = requireActivity().getCompatDrawable(R.color.colorWhite)

        setupFilters()
        setupViews()

        observe(screenViewModel.userFilters, ::handleUserFilters)
        observe(screenViewModel.availableFilters, ::handleAvailableFilters)

    }

    private fun handleAvailableFilters(filterData: FilterData?) {
        filterData?.let { data ->
            val adapterList = mutableListOf<DisplayableItem>()

            data.advertType?.ifNotNullOrEmpty {
                adapterList.add(FilterTitle(getString(R.string.label_filter_type_advert)))
                adapterList.addAll(it.mapIndexed { index, s -> RealtyTypeItems(index, s) })
            }

            data.realtyType?.ifNotNullOrEmpty {
                adapterList.add(FilterTitle(getString(R.string.label_filter_type_realty)))
                adapterList.addAll(it.mapIndexed { index, s -> RealtyTypeItems(index, s) })
            }

            data.realtyRepair?.ifNotNullOrEmpty {
                adapterList.add(FilterTitle(getString(R.string.label_filter_repair)))
                adapterList.addAll(it.mapIndexed { index, s -> RealtyTypeItems(index, s) })
            }

            data.realtyCost?.let { setupSlider(it) }

            filterAdvertAdapter.setData(adapterList)

            screenViewModel.userFilters.refresh()
        }
    }

    private fun handleUserFilters(filterData: FilterData?) {

        filterData?.let {
            when (it.realtySort) {
                FilterSort.DATE_DESC -> rbFilterNewFirst.isChecked = true
                FilterSort.DATE_ASC -> rbFilterOldFirst.isChecked = true
                FilterSort.COST_ASC -> rbFilterPriceUp.isChecked = true
                FilterSort.COST_DESC -> rbFilterPriceDown.isChecked = true
            }

            it.realtyCost?.let {
                if (screenViewModel.availableFilters.value != null) {
                    sliderRealtyCost.setValues(
                        it.first.toFloat(),
                        it.second.toFloat()
                    )
                }

                setRealtyCost(sliderRealtyCost)
            }

            it.realtyTypeSell?.first?.let {
                chipSell.isChecked = it
            }

            it.realtyTypeSell?.second?.let {
                chipRent.isChecked = it
            }

            setCheckableItems(
                listOf(
                    it.advertType.orEmpty(),
                    it.realtyType.orEmpty(),
                    it.realtyRepair.orEmpty()
                ).flatten(), filterAdvertAdapter
            )

        }
    }

    private fun setCheckableItems(list: List<String?>, adapter: CustomStableIdAdapter) {
        adapter.items?.forEach { item ->
            if (item is RealtyTypeItems) {
                if (list.contains(item.realtyTypeName)) {
                    item.checked = true
                    adapter.notifyDataSetChanged()
                } else {
                    item.checked = false
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setupSlider(realtyCost: Pair<Long, Long>) {

        sliderRealtyCost.valueTo = Float.MAX_VALUE
        sliderRealtyCost.valueFrom = Float.MIN_VALUE

        sliderRealtyCost.valueFrom = realtyCost.first.toFloat()
        sliderRealtyCost.valueTo =
            if (realtyCost.first == realtyCost.second) realtyCost.second.toFloat() + 1 else realtyCost.second.toFloat()

        sliderRealtyCost.setValues(
            realtyCost.first.toFloat(),
            realtyCost.second.toFloat()
        )

        setRealtyCost(sliderRealtyCost)
    }

    private fun setRealtyCost(slider: RangeSlider) {
        tvFilterCost.text = HtmlCompat.fromHtml(
            getString(
                R.string.placeholder_filter_cost,
                slider.values.getOrNull(0)?.roundToInt(),
                slider.values.getOrNull(1)?.roundToInt()
            ), HtmlCompat.FROM_HTML_MODE_COMPACT
        )
    }

    private fun setupViews() {
        tvButtonConfirm.setOnClickListener {
            screenViewModel.navigateToRequest()
        }

        rbFilterNewFirst.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                screenViewModel.setRealtySort(FilterSort.DATE_DESC)
            }
        }

        rbFilterOldFirst.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                screenViewModel.setRealtySort(FilterSort.DATE_ASC)
            }
        }

        rbFilterPriceUp.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                screenViewModel.setRealtySort(FilterSort.COST_ASC)
            }
        }

        rbFilterPriceDown.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                screenViewModel.setRealtySort(FilterSort.COST_DESC)
            }
        }

        sliderRealtyCost.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {

            override fun onStartTrackingTouch(slider: RangeSlider) {
                setRealtyCost(slider)
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                setRealtyCost(slider)
                screenViewModel.setRealtyCost(
                    slider.values.first().toLong() to slider.values.last().toLong()
                )
            }
        })

        chipSell.setOnCheckedChangeListener { group, checkedId ->
            screenViewModel.setRealtyTypeSell(chipSell.isChecked)
        }
        chipRent.setOnCheckedChangeListener { group, checkedId ->
            screenViewModel.setRealtyTypeRent(chipRent.isChecked)
        }
    }

    private fun setupFilters() {
        rvFilterAdvert.layoutManager = LinearLayoutManager(context)
        rvFilterAdvert.isNestedScrollingEnabled = false
        rvFilterAdvert.adapter = filterAdvertAdapter
    }

    companion object {

        const val FILTER_DATA = "FilterData"
        const val FILTER_CATEGORY = "FilterCategory"

        fun newInstance(category: String?, filterData: FilterData): FilterFragment =
            FilterFragment().apply {
                arguments = bundleOf(
                    FILTER_DATA to filterData,
                    FILTER_CATEGORY to category
                )
            }
    }
}
