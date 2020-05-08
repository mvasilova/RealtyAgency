package com.realtyagency.tm.presentation.viewmedia

import com.realtyagency.tm.app.platform.BaseViewModel
import com.realtyagency.tm.app.platform.NavigationEvent

class ViewMediaFilesViewModel : BaseViewModel() {

    fun onExit() {
        navigate(NavigationEvent.Exit)
    }
}