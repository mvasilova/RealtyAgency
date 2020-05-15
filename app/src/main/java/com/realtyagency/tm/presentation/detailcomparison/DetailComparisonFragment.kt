package com.realtyagency.tm.presentation.detailcomparison

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.observe
import com.realtyagency.tm.app.extensions.toDateFormatGmt
import com.realtyagency.tm.app.extensions.toStringOrNotData
import com.realtyagency.tm.app.platform.BaseFragment
import com.realtyagency.tm.data.db.entities.Comparison
import kotlinx.android.synthetic.main.fragment_detail_comparison.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.NumberFormat

class DetailComparisonFragment : BaseFragment(R.layout.fragment_detail_comparison) {

    override val toolbarTitle: Any?
        get() = getString(R.string.placeholder_comparison_name, idComparison)

    override val toolbarDrawableClose: Int?
        get() = R.drawable.ic_toolbar_back

    override val screenViewModel by viewModel<DetailComparisonViewModel>() {
        parametersOf(idComparison)
    }

    private val idComparison by lazy { arguments?.getInt(DETAIL_COMPARISON) }

    private val adapter by lazy {
        TableAdapter(requireContext()) {
            screenViewModel.removeItemRealtyToList(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tableComparison.setAdapter(adapter)
        tableComparison.rowHeaderWidth = resources.getDimensionPixelSize(R.dimen.table_row_width)

        observe(screenViewModel.comparisons, ::handleComparisons)
    }

    private fun handleComparisons(comparison: Comparison?) {
        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0

        val columns = comparison?.realty.orEmpty()
        val rows = resources.getStringArray(R.array.realty_parameters).asList()
        val matrix: MutableList<MutableList<String>> = mutableListOf()

        matrix.apply {
            addAll(
                0,
                mutableListOf(columns.map { format.format(it.parameters?.cost) ?: "" }
                    .toMutableList())
            )
            addAll(
                1,
                mutableListOf(columns.map { it.typeAdvert.toStringOrNotData() }.toMutableList())
            )
            addAll(
                2,
                mutableListOf(columns.map {
                    it.parameters?.datePublic?.toDateFormatGmt(requireContext())
                        .toStringOrNotData()
                }.toMutableList())
            )
            addAll(
                3,
                mutableListOf(columns.map { it.parameters?.countRoom.toStringOrNotData() }
                    .toMutableList())
            )
            addAll(
                4,
                mutableListOf(columns.map { it.parameters?.countFloor.toStringOrNotData() }
                    .toMutableList())
            )
            addAll(
                5,
                mutableListOf(columns.map { it.parameters?.floorFlat.toStringOrNotData() }
                    .toMutableList())
            )
            addAll(
                6,
                mutableListOf(columns.map {
                    getString(
                        R.string.placeholder_sq_metres,
                        it.parameters?.squareHome.toStringOrNotData()
                    )
                }.toMutableList())
            )
            addAll(
                7,
                mutableListOf(columns.map {
                    getString(
                        R.string.placeholder_acres,
                        it.parameters?.squarePlot.toStringOrNotData()
                    )
                }.toMutableList())
            )
            addAll(
                8,
                mutableListOf(columns.map {
                    it.parameters?.typeHousing.toStringOrNotData()
                }.toMutableList())
            )
            addAll(
                9,
                mutableListOf(columns.map {
                    it.parameters?.liftEnabled.toStringOrNotData()
                }.toMutableList())
            )
            addAll(
                10,
                mutableListOf(columns.map {
                    it.parameters?.repair.toStringOrNotData()
                }.toMutableList())
            )
            addAll(
                11,
                mutableListOf(columns.map {
                    getString(
                        R.string.placeholder_kilometres,
                        it.parameters?.distanceFromCenter.toStringOrNotData()
                    )
                }.toMutableList())
            )
            addAll(
                12,
                mutableListOf(columns.map {
                    it.parameters?.yearOfConstruction.toStringOrNotData()
                }.toMutableList())
            )
            addAll(
                13,
                mutableListOf(columns.map {
                    it.address.toStringOrNotData()
                }.toMutableList())
            )
            addAll(
                14,
                mutableListOf(columns.map {
                    it.description.toStringOrNotData()
                }.toMutableList())
            )
        }


        adapter.setAllItems(
            columns,
            rows,
            matrix as List<MutableList<String?>>? ?: mutableListOf()
        )
    }

    companion object {
        private const val DETAIL_COMPARISON: String = "DetailRealty"
        fun newInstance(idComparison: Int) =
            DetailComparisonFragment().apply {
                arguments = bundleOf(DETAIL_COMPARISON to idComparison)
            }
    }
}