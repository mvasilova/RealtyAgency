package com.realtyagency.tm.presentation.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.toDateFormatGmt
import com.realtyagency.tm.app.platform.DiffItem
import com.realtyagency.tm.data.db.entities.Comparison
import kotlinx.android.synthetic.main.item_comparison.*

fun comparisonsDelegate(clickListener: (Int) -> Unit) =
    adapterDelegateLayoutContainer<Comparison, DiffItem>(R.layout.item_comparison) {

        containerView.setOnClickListener {
            clickListener.invoke(item.idComparison)
        }

        bind {
            tvComparisonTitle.text =
                context.getString(R.string.placeholder_comparison_name, item.idComparison)

            tvComparisonDate.text = item.date?.toDateFormatGmt(context, getString(R.string.date_pattern_dmy_time))
        }
    }