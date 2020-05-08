package com.realtyagency.tm.presentation.common

import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.realtyagency.tm.app.platform.DisplayableItem

class CustomStableIdAdapter(vararg delegates: AdapterDelegate<List<DisplayableItem>>) :
    ListDelegationAdapter<List<DisplayableItem>>(*delegates) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return items[position].itemId.hashCode().toLong()
    }
}