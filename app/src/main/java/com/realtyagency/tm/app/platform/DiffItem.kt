package com.realtyagency.tm.app.platform

import androidx.recyclerview.widget.DiffUtil

interface DiffItem {
    val itemId: Long
}

val DIFF_CALLBACK: DiffUtil.ItemCallback<DiffItem> =
    object : DiffUtil.ItemCallback<DiffItem>() {
        override fun areItemsTheSame(oldItem: DiffItem, newItem: DiffItem): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        override fun areContentsTheSame(oldItem: DiffItem, newItem: DiffItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }