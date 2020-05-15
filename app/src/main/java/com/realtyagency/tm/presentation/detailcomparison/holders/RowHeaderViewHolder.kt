package com.realtyagency.tm.presentation.detailcomparison.holders

import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.realtyagency.tm.R
import kotlinx.android.synthetic.main.layout_table_view_row_header.view.*

class RowHeaderViewHolder(val view: View) : AbstractViewHolder(view) {

    fun setRowModel(text: String?, position: Int) {
        view.tvRowHeader.text = text

        view.viewTopRow.isVisible = position == 0

        if (position == 14) {
            view.containerRow.layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
        } else {
            view.containerRow.layoutParams.height =
                view.context.resources.getDimensionPixelSize(R.dimen.table_row_height)
        }
    }
}