package com.realtyagency.tm.presentation.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.SupportMapFragment

class ScrollMapFragment : SupportMapFragment() {
    var touchListener: (() -> Unit)? = null

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        savedInstance: Bundle?
    ): View {
        val layout = super.onCreateView(layoutInflater, viewGroup, savedInstance)
        val frameLayout = TouchableWrapper(requireActivity())

        frameLayout.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )

        (layout as ViewGroup).addView(
            frameLayout,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        return layout
    }

    fun setListener(listener: () -> Unit) {
        touchListener = listener
    }

    inner class TouchableWrapper(context: Context) : FrameLayout(context) {

        override fun dispatchTouchEvent(event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> touchListener?.invoke()
                MotionEvent.ACTION_UP -> touchListener?.invoke()
            }
            return super.dispatchTouchEvent(event)
        }
    }
}