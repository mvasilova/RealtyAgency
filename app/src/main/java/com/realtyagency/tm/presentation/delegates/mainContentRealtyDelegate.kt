package com.realtyagency.tm.presentation.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.realtyagency.tm.R
import com.realtyagency.tm.data.db.entities.Realty
import kotlinx.android.synthetic.main.item_realty.tvCost
import kotlinx.android.synthetic.main.item_realty.tvName
import kotlinx.android.synthetic.main.item_realty_detail.*
import java.text.NumberFormat

fun mainContentRealtyDelegate(
    clickListenerOnCall: (Int) -> Unit,
    clickListenerOnAddComparison: (Realty) -> Unit
) =
    adapterDelegateLayoutContainer<Realty, Any>(R.layout.item_realty_detail) {

        btnCall.setOnClickListener {
            clickListenerOnCall.invoke(item.id) //TODO
        }

        btnAddComparison.setOnClickListener {
            clickListenerOnAddComparison.invoke(item)
        }

        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0

        bind {
            tvName.text = item.name
            tvCost.text = format.format(item.parameters?.cost)
            tvDescription.text = item.description
        }
    }