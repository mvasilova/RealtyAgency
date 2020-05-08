package com.realtyagency.tm.presentation.common

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.graphics.withTranslation
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecoration(
    private val strokeColor: Int,
    private val strokeWidth: Int,
    private val marginStart: Float = 0.0F,
    private val marginEnd: Float = marginStart,
    private val horizontalSpacing: Int = 0,
    private val verticalSpacing: Int = horizontalSpacing
) : RecyclerView.ItemDecoration() {

    private val dividerPaint = Paint().apply {
        color = strokeColor
        strokeWidth = this@DividerItemDecoration.strokeWidth.toFloat()
        flags = Paint.ANTI_ALIAS_FLAG
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(horizontalSpacing, verticalSpacing, horizontalSpacing, verticalSpacing)
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent.children.forEach { child ->
            canvas.withTranslation(
                x = 0.0F,
                y = verticalSpacing.toFloat()
            ) {
                val lineTopY = child.top - strokeWidth / 2f
                drawLine(
                    child.left + marginStart,
                    lineTopY,
                    child.right - marginEnd,
                    lineTopY,
                    dividerPaint
                )
            }
        }
    }
}