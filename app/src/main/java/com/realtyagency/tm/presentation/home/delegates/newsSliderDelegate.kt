package com.realtyagency.tm.presentation.home.delegates

import androidx.core.view.updateLayoutParams
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.realtyagency.tm.BuildConfig
import com.realtyagency.tm.R
import com.realtyagency.tm.app.di.module.GlideApp
import com.realtyagency.tm.app.extensions.getCompatColor
import com.realtyagency.tm.app.extensions.getCompatDrawable
import com.realtyagency.tm.app.extensions.screenWidth
import com.realtyagency.tm.data.entities.News
import kotlinx.android.synthetic.main.item_slider_news.view.*

fun newsSliderDelegate(clickListener: (News) -> Unit) =
    adapterDelegateLayoutContainer<News, Any>(R.layout.item_slider_news) {

        containerView.setOnClickListener {
            clickListener(item)
        }

        bind {
            containerView.updateLayoutParams {
                width = (context.screenWidth * 0.85f).toInt()
            }
            containerView.tvNewsTitle.text = item.header
            if (!item.attachments.isNullOrEmpty()) {
                GlideApp.with(containerView.context)
                    .load(BuildConfig.API_ENDPOINT + item.attachments?.get(0)?.path)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .optionalCenterCrop()
                    .into(containerView.ivNews)
            } else {
                containerView.ivNews.setImageDrawable(context.getCompatDrawable(R.drawable.bg_placeholder_news))
                containerView.ivNews.setBackgroundColor(context.getCompatColor(R.color.colorNewsPlaceholder))
            }
        }
    }