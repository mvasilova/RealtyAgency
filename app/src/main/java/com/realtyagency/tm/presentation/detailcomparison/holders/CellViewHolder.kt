package com.realtyagency.tm.presentation.detailcomparison.holders

import android.view.View
import android.widget.LinearLayout
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.realtyagency.tm.R
import kotlinx.android.synthetic.main.layout_table_view_cell.view.*

class CellViewHolder(private val view: View) : AbstractViewHolder(view) {

    fun setCellModel(model: String?, position: Int) {

        view.tvCellData.text = model

        if (position == 14) {
            view.containerCellData.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        } else {
            view.containerCellData.layoutParams.height =
                view.context.resources.getDimensionPixelSize(R.dimen.table_row_height)
        }
    }
}