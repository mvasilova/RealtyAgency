package com.realtyagency.tm.presentation.detailcomparison.holders

import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.evrencoskun.tableview.ITableView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder
import com.realtyagency.tm.R
import com.realtyagency.tm.app.di.module.GlideApp
import com.realtyagency.tm.data.db.entities.Realty
import kotlinx.android.synthetic.main.layout_table_view_column_header.view.*

class ColumnHeaderViewHolder(
    val view: View,
    private val tableView: ITableView,
    private val clickListener: (Realty) -> Unit
) : AbstractSorterViewHolder(view) {

    private val heightRecyclerView =
        view.context.resources.getDimensionPixelSize(R.dimen.table_column_height)

    fun setColumnHeaderModel(model: Realty?, position: Int) {

        view.tvColumnHeader.text = model?.name

        GlideApp.with(view.tvColumnHeaderPhoto.context)
            .load(model?.photos?.get(0))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .optionalCenterCrop()
            .into(view.tvColumnHeaderPhoto)

        view.viewStartColumn.isVisible = position == 0

        view.ivDelete.setOnClickListener {
            model?.let(clickListener)
        }

        tableView.columnHeaderRecyclerView.layoutParams.height = heightRecyclerView
        tableView.cellRecyclerView.updateLayoutParams<FrameLayout.LayoutParams> {
            topMargin = heightRecyclerView
        }
        tableView.rowHeaderRecyclerView.updateLayoutParams<FrameLayout.LayoutParams> {
            topMargin = heightRecyclerView
        }

    }
}