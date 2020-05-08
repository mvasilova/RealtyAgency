package com.realtyagency.tm.presentation.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.realtyagency.tm.R
import kotlinx.android.synthetic.main.item_realty_features.*

fun featuresContentRealtyDelegate() =
    adapterDelegateLayoutContainer<Pair<String, Any>, Any>(R.layout.item_realty_features) {

        bind {
            tvFeatureName.text = item.first
            tvFeatureDescription.text = item.second.toString()
        }
    }