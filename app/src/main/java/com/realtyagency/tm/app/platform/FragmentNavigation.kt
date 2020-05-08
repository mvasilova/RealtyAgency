package com.realtyagency.tm.app.platform

import androidx.fragment.app.DialogFragment
import com.ncapdevi.fragnav.FragNavController

/**
 * Proxy interface to [FragNavController]
 * */
interface FragmentNavigation {

    /**
     * Switch tab in bottom bar
     * */
    fun switchTab(index: Int)

    /**
     * You can only push onto the currently selected index
     * */
    fun pushFragment(fragment: BaseFragment)

    /**
     * Pop the current fragment from the current tab
     * */
    fun popFragment()

    /**
     * If the pop depth is deeper than possible, it will stop when it gets to the root fragment
     * */
    fun popFragments(count: Int)

    /**
     * Clear the stack to bring you back to the base fragment
     * */
    fun clearStack()

    /**
     * Use this method to navigate dialogs
     * */
    fun showDialogFragment(dialogFragment: DialogFragment)

    /**
     * If current stack > 1
     * */
    fun canGoBack(): Boolean

    fun showBottomNavigation()

    fun hideBottomNavigation()
}