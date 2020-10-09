package com.realtyagency.tm.presentation.delegates

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.realtyagency.tm.R
import com.realtyagency.tm.app.di.module.GlideApp
import com.realtyagency.tm.app.extensions.formatToCurrency
import com.realtyagency.tm.app.extensions.getCompatColor
import com.realtyagency.tm.app.extensions.getCompatDrawable
import com.realtyagency.tm.app.platform.DiffItem
import com.realtyagency.tm.data.db.entities.Realty
import kotlinx.android.synthetic.main.item_realty.*

fun realtyDelegate(clickListener: (Realty) -> Unit, clickListenerFavorite: (Realty) -> Unit) =
    adapterDelegateLayoutContainer<Realty, DiffItem>(R.layout.item_realty) {

        containerView.setOnClickListener {
            clickListener.invoke(item)
        }

        ivFavorite.setOnClickListener {
            clickListenerFavorite.invoke(item)
        }

        bind {
            tvName.text = item.name
            tvCost.text = item.parameters?.cost.formatToCurrency()
            GlideApp.with(context)
                .load(item.photos?.get(0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .optionalCenterCrop()
                .placeholder(R.drawable.ic_placeholder_request)
                .into(DrawableImageViewTarget(ivImage).waitForLayout())

            llCard.setBackgroundColor(
                if (item.premium == true) {
                    context.getCompatColor(R.color.colorLightYellow)
                } else {
                    context.getCompatColor(R.color.colorWhite)
                }
            )

            ivFavorite.background = if (item.isFavorite == true) {
                context.getCompatDrawable(R.drawable.ic_favorite_fill)
            } else {
                context.getCompatDrawable(R.drawable.ic_favorite_border)
            }
        }
    }