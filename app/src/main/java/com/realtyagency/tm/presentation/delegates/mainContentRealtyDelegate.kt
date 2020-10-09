package com.realtyagency.tm.presentation.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.formatToCurrency
import com.realtyagency.tm.data.db.entities.Realty
import kotlinx.android.synthetic.main.item_realty.tvCost
import kotlinx.android.synthetic.main.item_realty.tvName
import kotlinx.android.synthetic.main.item_realty_detail.*

fun mainContentRealtyDelegate(
    clickListenerOnCall: (Int) -> Unit,
    clickListenerOnAddComparison: () -> Unit
) =
    adapterDelegateLayoutContainer<Realty, Any>(R.layout.item_realty_detail) {

        btnCall.setOnClickListener {
            clickListenerOnCall.invoke(item.id) //TODO
        }

        btnAddComparison.setOnClickListener {
            clickListenerOnAddComparison.invoke()
        }

        bind {
            tvName.text = item.name
            tvCost.text = item.parameters?.cost.formatToCurrency()
            tvDescription.text = item.description
        }
    }