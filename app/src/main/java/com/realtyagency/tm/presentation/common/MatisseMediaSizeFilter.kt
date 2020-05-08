package com.realtyagency.tm.presentation.common

import android.content.Context
import com.realtyagency.tm.R
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.filter.Filter
import com.zhihu.matisse.internal.entity.IncapableCause
import com.zhihu.matisse.internal.entity.Item

internal class MatisseMediaSizeFilter : Filter() {

    public override fun constraintTypes() = setOf(MimeType.PNG, MimeType.JPEG, MimeType.MP4)

    override fun filter(context: Context, item: Item): IncapableCause? {
        var message: String? = null
        if (item.mimeType.contains("png") || item.mimeType.contains("jpeg")) {
            if (item.size > MAX_IMAGE_SIZE_10_MB) {
                message = context.getString(R.string.dialog_message_size_image_too_much)
            }
        } else if (item.mimeType.contains("mp4")) {
            if (item.size > MAX_VIDEO_SIZE_50_MB) {
                message = context.getString(R.string.dialog_message_size_video_too_much)
            }
        }
        return if (!message.isNullOrEmpty())
            IncapableCause(IncapableCause.TOAST, (message)) else null
    }

    companion object {
        private const val MAX_IMAGE_SIZE_10_MB = 10485760
        private const val MAX_VIDEO_SIZE_50_MB = 52428800
    }
}