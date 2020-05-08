package com.realtyagency.tm.presentation.common

import android.content.Context
import android.widget.ArrayAdapter
import com.realtyagency.tm.R


class CommonSpinnerAdapter(
    context: Context,
    textViewResource: Int = R.layout.item_spinner_full_width,
    dropDownViewResource: Int = R.layout.item_spinner_dropdown_checkable
) : ArrayAdapter<String>(context, textViewResource) {

    init {
        setDropDownViewResource(dropDownViewResource)
    }
}