package com.realtyagency.tm.presentation.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavController.Companion.TAB1
import com.ncapdevi.fragnav.FragNavController.Companion.TAB2
import com.ncapdevi.fragnav.FragNavController.Companion.TAB3
import com.ncapdevi.fragnav.FragNavSwitchController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import com.ncapdevi.fragnav.tabhistory.UniqueTabHistoryStrategy
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.gone
import com.realtyagency.tm.app.extensions.observeEvent
import com.realtyagency.tm.app.extensions.show
import com.realtyagency.tm.app.platform.BaseActivity
import com.realtyagency.tm.app.platform.BaseFragment
import com.realtyagency.tm.app.platform.FragmentNavigation
import com.realtyagency.tm.presentation.comparison.ComparisonsFragment
import com.realtyagency.tm.presentation.favorite.FavoritesFragment
import com.realtyagency.tm.presentation.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(R.layout.activity_main), FragNavController.RootFragmentListener,
    FragmentNavigation {

    private val viewModel: MainViewModel by viewModel()

    override val screenViewModel by lazy { viewModel }

    private val fragNavController by lazy {
        FragNavController(
            supportFragmentManager,
            R.id.container
        )
    }

    private val tabs = arrayListOf(
        R.id.tab_home to HomeFragment(),
        R.id.tab_comparison to ComparisonsFragment(),
        R.id.tab_favorites to FavoritesFragment()
    )

    override fun initInterface(savedInstanceState: Bundle?) {
        setupBottomNavigation(savedInstanceState)
        setupObservers()
    }

    private fun setupBottomNavigation(savedInstanceState: Bundle?) {

        val navigationItemListener = BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tab_home -> fragNavController.switchTab(TAB1)
                R.id.tab_comparison -> fragNavController.switchTab(TAB2)
                R.id.tab_favorites -> fragNavController.switchTab(TAB3)
            }
            true
        }

        fragNavController.apply {
            rootFragmentListener = this@MainActivity
            fragmentHideStrategy = FragNavController.DETACH
            navigationStrategy = UniqueTabHistoryStrategy(object : FragNavSwitchController {
                override fun switchTab(index: Int, transactionOptions: FragNavTransactionOptions?) {
                    bottomNavigation.selectedItemId = tabs[index].first
                }
            })
        }

        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemListener)
        bottomNavigation.setOnNavigationItemReselectedListener { fragNavController.clearStack() }

        fragNavController.initialize(TAB1, savedInstanceState)
    }

    private fun setupObservers() {
        observeEvent(viewModel.mainState, ::handleState)
    }

    override val numberOfRootFragments = tabs.size

    override fun getRootFragment(index: Int) = tabs[index].second

    override fun switchTab(index: Int) {
        bottomNavigation.selectedItemId = tabs[index].first
        fragNavController.switchTab(index)
    }

    override fun pushFragment(fragment: BaseFragment) {
        fragNavController.pushFragment(fragment)
    }

    override fun popFragment() {
        fragNavController.popFragment()
    }

    override fun popFragments(count: Int) {
        fragNavController.popFragments(count)
    }

    override fun clearStack() {
        fragNavController.clearStack()
    }

    override fun showDialogFragment(dialogFragment: DialogFragment) {
        fragNavController.showDialogFragment(dialogFragment)
    }

    override fun canGoBack(): Boolean {
        return fragNavController.currentStack?.size?.let { size ->
            size > 1
        } ?: false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        fragNavController.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        val topFragment = fragNavController.currentStack?.peek()
        when {
            topFragment is BaseFragment && topFragment.onBackPressed() -> Unit
            fragNavController.popFragment() -> Unit
            else -> super.onBackPressed()
        }
    }

    override fun hideBottomNavigation() {
        if (bottomNavigation.visibility != View.GONE) {
            bottomNavigation?.gone()
        }
    }

    override fun showBottomNavigation() {
        if (bottomNavigation.visibility != View.VISIBLE) {
            bottomNavigation?.show()
        }
    }
}
