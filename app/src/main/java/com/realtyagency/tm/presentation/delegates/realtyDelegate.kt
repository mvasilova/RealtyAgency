package com.realtyagency.tm.presentation.delegates

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.realtyagency.tm.R
import com.realtyagency.tm.app.di.module.GlideApp
import com.realtyagency.tm.app.extensions.getCompatColor
import com.realtyagency.tm.app.extensions.getCompatDrawable
import com.realtyagency.tm.app.platform.DiffItem
import com.realtyagency.tm.data.db.entities.Realty
import kotlinx.android.synthetic.main.item_realty.*
import java.text.NumberFormat

fun realtyDelegate(clickListener: (Realty) -> Unit, clickListenerFavorite: (Realty) -> Unit) =
    adapterDelegateLayoutContainer<Realty, DiffItem>(R.layout.item_realty) {

        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0

        containerView.setOnClickListener {
            clickListener.invoke(item)
        }

        ivFavorite.setOnClickListener {
            clickListenerFavorite.invoke(item)
        }

        bind {
            tvName.text = item.name
            tvCost.text = format.format(item.parameters?.cost)
            GlideApp.with(context)
                .load(item.photos?.get(0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .optionalCenterCrop()
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