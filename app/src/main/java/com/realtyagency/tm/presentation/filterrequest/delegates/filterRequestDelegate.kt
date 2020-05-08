package com.realtyagency.tm.presentation.filterrequest.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.realtyagency.tm.R
import com.realtyagency.tm.app.platform.DisplayableItem
import kotlinx.android.synthetic.main.item_filter_type_request.view.*

fun filterRequestDelegate(clickListener: (RequestTypeItems) -> Unit) =
    adapterDelegateLayoutContainer<RequestTypeItems, DisplayableItem>(R.layout.item_filter_type_request) {

        containerView.setOnClickListener {
            clickListener(item)
        }

        bind {
            containerView.tvItemName.text = item.requestTypeName
            containerView.cbFilterItem.isChecked = item.checked
        }
    }

data class RequestTypeItems(
    val typeId: String,
    val requestTypeName: String,
    var checked: Boolean = false
) : DisplayableItem {
    override val itemId = typeId
}
