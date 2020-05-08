package com.realtyagency.tm.app.platform

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.airbnb.paris.extensions.style
import com.google.android.material.snackbar.Snackbar
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.*
import com.realtyagency.tm.app.platform.NavigationEvent.*
import com.realtyagency.tm.presentation.common.FullScreenDialog
import com.realtyagency.tm.presentation.common.FullScreenDialog.Companion.DIALOG_FULL_SCREEN_TAG
import kotlinx.android.synthetic.main.layout_progress.view.*
import kotlinx.android.synthetic.main.toolbar_with_icon.*


/**
 * Base Fragment class with helper methods for handling views, handle navigation and base events.
 */
abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    open val supportFragmentManager
        get() = requireActivity().supportFragmentManager

    open val screenViewModel: BaseViewModel?
        get() = null

    open val showBottomNavWhenFragmentLaunch = true
    open val statusBarColor = R.color.colorStatusBar
    open val statusBarLightMode = true

    open val toolbarTitle: Any? = null
    open val toolbarTextButtonConfirm: Int? = null
    open val toolbarDrawableClose: Int? = null
    open val toolbarIconFilterVisible: Boolean = false

    private var fragmentNavigation: FragmentNavigation? = null
    private var snackBar: Snackbar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentNavigation = context as? FragmentNavigation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.setStatusBarColor(requireActivity().getCompatColor(statusBarColor))
        activity?.setStatusBarLightMode(statusBarLightMode)

        if (showBottomNavWhenFragmentLaunch) {
            fragmentNavigation?.showBottomNavigation()
        } else {
            fragmentNavigation?.hideBottomNavigation()
        }

        setToolbarTitle()

        observeBaseLiveData()
    }

    open fun observeBaseLiveData() {
        screenViewModel?.let { vm ->
            observeEvent(vm.mainState, ::handleState)
            observeEvent(vm.message, ::showToast)
            observeEvent(vm.fragmentNavigation, ::handleFragmentNavigation)
        }
    }


    override fun onDestroyView() {
        snackBar?.dismiss()
        super.onDestroyView()
    }

    private fun handleFragmentNavigation(event: NavigationEvent) {
        when (event) {
            ClearStack -> fragmentNavigation?.clearStack()
            Exit -> fragmentNavigation?.popFragment()
            is SwitchTab -> fragmentNavigation?.switchTab(event.tabPosition)
            is PopFragments -> fragmentNavigation?.popFragments(event.count)
            is PushFragment -> {
                if (event.clearStack) fragmentNavigation?.clearStack()
                fragmentNavigation?.pushFragment(event.fragment)
            }
        }
    }

    fun exit() = fragmentNavigation?.popFragment()

    internal fun dialogNotAlreadyShown(tag: String) =
        supportFragmentManager.findFragmentByTag(tag) == null

    open fun handleState(state: State) {
        snackBar?.dismiss()
        when (state) {
            is State.Loading -> {
                progressStatus(View.VISIBLE)
            }
            is State.Loaded -> {
                progressStatus(View.GONE)
            }
            is State.Error -> {
                progressStatus(View.GONE)
                handleFailure(state.failure)
            }
        }
    }

    private fun progressStatus(visibility: Int) = view?.let {
        it.progressLayout?.visibility = visibility
    }

    private fun setToolbarTitle() {
        if (toolbarTitle is Int && toolbarTitle != null) {
            tvToolbarTitle?.text = resources.getString(toolbarTitle as Int)
        } else if ((toolbarTitle is String)) {
            tvToolbarTitle?.text = toolbarTitle.toString()
        }
        ivFilterRequests?.isVisible = toolbarIconFilterVisible

        if (toolbarTextButtonConfirm != null) {
            tvButtonConfirm?.text = resources.getString(toolbarTextButtonConfirm!!)
        } else {
            tvButtonConfirm?.gone()
        }

        if (toolbarDrawableClose != null) {
            ivClose?.setImageDrawable(resources.getDrawable(toolbarDrawableClose!!, null))
        } else {
            ivClose?.gone()
        }

        if (!statusBarLightMode) {
            toolbar?.setBackgroundColor(resources.getColor(R.color.colorPrimary, null))
            tvToolbarTitle?.style(R.style.Text_Bold_White_20)
            viewToolbar?.setBackgroundColor(resources.getColor(R.color.colorWhiteTransparent, null))
        }

        ivClose?.setOnClickListener {
            fragmentNavigation?.popFragment()
        }
    }

    internal fun notify(@StringRes title: Int, @StringRes message: Int) {
        if (dialogNotAlreadyShown(CommonDialog.TAG)) {
            CommonDialog.Builder()
                .cancelVisible(false)
                .btnOk(R.string.btn_ok)
                .title(getString(title))
                .message(getString(message))
                .tag(DIALOG_ERROR_TAG)
                .cancelable(false)
                .build()
                .apply {
                    setTargetFragment(this@BaseFragment, 0)
                }
                .show(supportFragmentManager, CommonDialog.TAG)
        }
    }

    override fun onStop() {
        super.onStop()
        hideSoftKeyboard()
    }

    fun showSoftKeyboard(field: View?) {
        field?.let {
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun hideSoftKeyboard() {
        view?.let {
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    protected fun setKeyboardModPan() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    protected fun setKeyboardModResize() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    protected fun setKeyboardModResizeAndHidden() {
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                    or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
    }

    open fun loadData() {}

    //override `true` if fragment needs custom navigation
    open fun onBackPressed(): Boolean = false

    open fun handleOnlyFailure(state: State?) {
        if (state is State.Error) {
            handleFailure(state.failure)
        }
    }

    open fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.ServerError -> if (dialogNotAlreadyShown(DIALOG_FULL_SCREEN_TAG)) FullScreenDialog.newInstance(
                R.layout.dialog_server_error,
                R.string.label_server_error,
                R.string.label_server_error_description
            ).show(supportFragmentManager, DIALOG_FULL_SCREEN_TAG)
            is Failure.CommonError -> if (dialogNotAlreadyShown(DIALOG_FULL_SCREEN_TAG)) FullScreenDialog.newInstance(
                R.layout.dialog_server_error,
                R.string.label_common_error,
                R.string.label_common_error_description
            ).show(supportFragmentManager, DIALOG_FULL_SCREEN_TAG)
        }
    }

    companion object {
        const val DIALOG_ERROR_TAG = "DialogError"
    }
}
