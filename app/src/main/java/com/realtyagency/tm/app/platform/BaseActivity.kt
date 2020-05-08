package com.realtyagency.tm.app.platform

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.*
import com.realtyagency.tm.presentation.common.FullScreenDialog
import com.realtyagency.tm.presentation.common.FullScreenDialog.Companion.DIALOG_FULL_SCREEN_TAG
import kotlinx.android.synthetic.main.layout_progress.*


abstract class BaseActivity(@LayoutRes val layoutResId: Int) : AppCompatActivity() {

    open val screenViewModel: BaseViewModel?
        get() = null

    open val statusBarColor = R.color.colorStatusBar
    open val statusBarLightMode = true

    private val rootView
        get() = (this.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup

    private var snackBar: Snackbar? = null

    abstract fun initInterface(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)

        this.setStatusBarColor(this.getCompatColor(statusBarColor))
        this.setStatusBarLightMode(statusBarLightMode)

        initInterface(savedInstanceState)
        observeBaseLiveData()
    }

    open fun observeBaseLiveData() {
        screenViewModel?.let { vm ->
            observeEvent(vm.mainState, ::handleState)
            observeEvent(vm.message, ::showToast)
        }
    }

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

    private fun progressStatus(visibility: Int) {
        progressLayout?.visibility = visibility
    }

    internal fun dialogNotAlreadyShown(tag: String) =
        supportFragmentManager.findFragmentByTag(tag) == null

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
                .show(supportFragmentManager, CommonDialog.TAG)
        }
    }

    open fun loadData() {}

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

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    companion object {
        const val DIALOG_ERROR_TAG = "DialogError"
    }
}
