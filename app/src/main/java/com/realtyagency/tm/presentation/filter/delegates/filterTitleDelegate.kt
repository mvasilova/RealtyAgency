package com.realtyagency.tm.presentation.filter.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.realtyagency.tm.R
import com.realtyagency.tm.app.platform.DisplayableItem
import kotlinx.android.synthetic.main.item_filter_title.view.*

fun filterTitleDelegate() =
    adapterDelegateLayoutContainer<FilterTitle, DisplayableItem>(R.layout.item_filter_title) {

        bind {
            containerView.tvFilterTitle.text = item.title
        }
    }

data class FilterTitle(
    val title: String
) : DisplayableItem {
    override val itemId = title.hashCode().toString()
}
