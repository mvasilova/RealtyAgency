package com.realtyagency.tm.app.platform

sealed class NavigationEvent {

    //назад
    object Exit : NavigationEvent()

    //очистить стек текущей вкладки
    object ClearStack : NavigationEvent()

    //достать фрагменты
    data class PopFragments(val count: Int) : NavigationEvent()

    //переключить вкладку
    data class SwitchTab(val tabPosition: Int) : NavigationEvent()

    //открыть новый фрагмент
    data class PushFragment(
        val fragment: BaseFragment,
        val clearStack: Boolean = false
    ) : NavigationEvent()
}