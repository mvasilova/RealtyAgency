package com.realtyagency.tm.presentation.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.realtyagency.tm.R
import kotlinx.android.synthetic.main.item_category.view.*

fun categoryDelegate(clickListener: (String) -> Unit) =
    adapterDelegateLayoutContainer<String, Any>(R.layout.item_category) {

        containerView.setOnClickListener {
            clickListener.invoke(item)
        }

        bind {
            containerView.tvCategoryName.text = item
        }
    }