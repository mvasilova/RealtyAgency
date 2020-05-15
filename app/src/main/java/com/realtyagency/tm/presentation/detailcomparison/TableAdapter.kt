package com.realtyagency.tm.presentation.detailcomparison

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.realtyagency.tm.R
import com.realtyagency.tm.data.db.entities.Realty
import com.realtyagency.tm.presentation.detailcomparison.holders.CellViewHolder
import com.realtyagency.tm.presentation.detailcomparison.holders.ColumnHeaderViewHolder
import com.realtyagency.tm.presentation.detailcomparison.holders.RowHeaderViewHolder

class TableAdapter(private val context: Context, private val clickListener: (Realty) -> Unit) :
    AbstractTableAdapter<Realty?, String?, String?>() {

    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val layout = LayoutInflater.from(context).inflate(
            R.layout.layout_table_view_cell,
            parent, false
        )
        return CellViewHolder(
            layout
        )
    }

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: String?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        (holder as CellViewHolder).setCellModel(cellItemModel, rowPosition)
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {
        val layout: View = LayoutInflater.from(context)
            .inflate(R.layout.layout_table_view_column_header, parent, false)
        return ColumnHeaderViewHolder(
            layout,
            tableView
        ) {
            clickListener.invoke(it)
        }
    }

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        columnHeaderItemModel: Realty?,
        columnPosition: Int
    ) {
        val columnHeaderViewHolder: ColumnHeaderViewHolder = holder as ColumnHeaderViewHolder
        columnHeaderViewHolder.setColumnHeaderModel(columnHeaderItemModel, columnPosition)
    }

    override fun onCreateRowHeaderViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {

        val layout: View = LayoutInflater.from(context).inflate(
            R.layout.layout_table_view_row_header,
            parent, false
        )

        return RowHeaderViewHolder(
            layout
        )
    }

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItemModel: String?,
        rowPosition: Int
    ) {
        val rowHeaderViewHolder: RowHeaderViewHolder = holder as RowHeaderViewHolder
        rowHeaderViewHolder.setRowModel(rowHeaderItemModel, rowPosition)
    }


    override fun onCreateCornerView(parent: ViewGroup): View {
        return LayoutInflater.from(context)
            .inflate(R.layout.layout_table_view_corner, parent, false)
    }

    override fun getColumnHeaderItemViewType(position: Int): Int {
        return 0
    }

    override fun getRowHeaderItemViewType(position: Int): Int {
        return 0
    }

    override fun getCellItemViewType(position: Int): Int {
        return 0
    }
}