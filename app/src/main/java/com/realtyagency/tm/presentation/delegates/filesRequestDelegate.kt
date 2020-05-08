package com.realtyagency.tm.presentation.delegates

import androidx.core.view.isVisible
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.realtyagency.tm.BuildConfig
import com.realtyagency.tm.R
import com.realtyagency.tm.app.di.module.GlideApp
import com.realtyagency.tm.data.entities.Attachment
import kotlinx.android.synthetic.main.item_request_file.view.*

fun filesRequestDelegate(showRemoveButton: Boolean, clickListener: (Attachment) -> Unit, clickListenerViewMedia: (Attachment) -> Unit) =
    adapterDelegateLayoutContainer<Attachment, Any>(R.layout.item_request_file) {

        containerView.ivRemoveFile?.setOnClickListener {
            clickListener(item)
        }

        containerView.setOnClickListener {
            clickListenerViewMedia(item)
        }

        bind {
            if (showRemoveButton) {
                GlideApp.with(containerView.context)
                    .load(item.pathCacheFile ?: item.pathFile)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_placeholder_request)
                    .optionalCenterCrop()
                    .into(containerView.ivFile)
            } else {
                GlideApp.with(containerView.context)
                    .load(BuildConfig.API_ENDPOINT + item.path)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_placeholder_request)
                    .optionalCenterCrop()
                    .into(containerView.ivFile)
            }

            containerView.ivRemoveFile.isVisible = showRemoveButton
        }
    }