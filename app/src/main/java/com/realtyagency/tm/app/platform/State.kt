package com.realtyagency.tm.app.platform

sealed class State {
    object Loading : State()
    object Loaded : State()
    data class Error(val failure: Failure) : State()
}
