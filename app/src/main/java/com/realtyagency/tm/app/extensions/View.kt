package com.realtyagency.tm.app.extensions

import android.os.SystemClock
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout?.isValid(): Boolean {
    val text = this?.editText?.text ?: ""
    return !(text.isEmpty() || text.length < 6 || text.contains(" "))
}

fun TextView.setTextOrHide(value: String?) {
    if (value.isNullOrEmpty()) {
        gone()
    } else {
        show()
        text = value
    }
}

fun Spinner.selectValue(value: String) {
    for (i in 0 until this.count) {
        if (this.getItemAtPosition(i) == value) {
            this.setSelection(i)
            break
        }
    }
}

fun View.clickWithDebounce(debounceTime: Long = 600L, action: () -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()

            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun View.show() = run { visibility = View.VISIBLE }

fun View.gone() = run { visibility = View.GONE }