package com.realtyagency.tm.presentation.delegates

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.realtyagency.tm.R
import com.realtyagency.tm.app.di.module.GlideApp
import kotlinx.android.synthetic.main.item_realty_detail_photo.*

fun realtyDetailPhotoDelegate(clickListener: (String) -> Unit) =
    adapterDelegateLayoutContainer<String, Any>(R.layout.item_realty_detail_photo) {

        containerView.setOnClickListener {
            clickListener.invoke(item)
        }

        bind {
            GlideApp.with(context)
                .load(item)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .optionalCenterCrop()
                .into(ivRealtyPhoto)
        }
    }