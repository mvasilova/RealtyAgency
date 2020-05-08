package com.realtyagency.tm.presentation.viewmedia.delegates

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.realtyagency.tm.R
import com.realtyagency.tm.app.di.module.GlideApp
import kotlinx.android.synthetic.main.item_view_image_files.view.*

fun viewImageFilesDelegate() =
    adapterDelegateLayoutContainer<String, Any>(R.layout.item_view_image_files) {

        bind {
            GlideApp.with(context)
                .load(item)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(containerView.ivViewPhoto)
        }
    }