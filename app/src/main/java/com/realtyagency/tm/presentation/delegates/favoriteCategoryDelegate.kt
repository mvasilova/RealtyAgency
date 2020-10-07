package com.realtyagency.tm.presentation.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.realtyagency.tm.R
import com.realtyagency.tm.app.platform.DiffItem
import kotlinx.android.synthetic.main.item_favorite_category.view.*

fun favoriteCategoryDelegate() =
    adapterDelegateLayoutContainer<Category, DiffItem>(R.layout.item_favorite_category) {

        bind {
            containerView.tvFavoriteCategory.text = item.name
        }
    }

data class Category(val name: String) : DiffItem {
    override val itemId = name.hashCode().toLong()
}