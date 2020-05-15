package com.realtyagency.tm.presentation.filter.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.realtyagency.tm.R
import com.realtyagency.tm.app.platform.DisplayableItem
import kotlinx.android.synthetic.main.item_filter_type_realty.view.*

fun filterRequestDelegate(clickListener: (RealtyTypeItems) -> Unit) =
    adapterDelegateLayoutContainer<RealtyTypeItems, DisplayableItem>(R.layout.item_filter_type_realty) {

        containerView.setOnClickListener {
            clickListener(item)
        }

        bind {
            containerView.tvItemName.text = item.realtyTypeName
            containerView.cbFilterItem.isChecked = item.checked
        }
    }

data class RealtyTypeItems(
    val typeId: Int,
    val realtyTypeName: String,
    var checked: Boolean = false
) : DisplayableItem {
    override val itemId = typeId.toString()
}
