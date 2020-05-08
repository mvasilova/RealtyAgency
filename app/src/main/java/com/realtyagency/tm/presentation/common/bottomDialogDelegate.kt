package com.realtyagency.tm.presentation.common

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.realtyagency.tm.R
import kotlinx.android.synthetic.main.item_dialog_bottom_sheet.view.*
import java.io.Serializable

fun bottomDialogDelegate(clickListener: (ItemDialog) -> Unit) =
    adapterDelegateLayoutContainer<ItemDialog, Any>(R.layout.item_dialog_bottom_sheet) {

        containerView.setOnClickListener {
            clickListener.invoke(item)
        }

        bind {
            containerView.tvDialogTitle.text = getString(item.title)
            containerView.tvDialogTitle.setCompoundDrawablesWithIntrinsicBounds(
                context.getDrawable(
                    item.drawable
                ), null, null, null
            )
        }
    }

data class ItemDialog(
    val drawable: Int,
    val title: Int
) : Serializable