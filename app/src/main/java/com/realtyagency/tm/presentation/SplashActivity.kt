package com.realtyagency.tm.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.realtyagency.tm.R
import com.realtyagency.tm.app.extensions.observeEvent
import com.realtyagency.tm.app.platform.BaseActivity
import com.realtyagency.tm.presentation.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity(R.layout.activity_splash) {

    override val statusBarColor: Int
        get() = R.color.colorPrimary

    override val statusBarLightMode: Boolean
        get() = false

    override val screenViewModel by viewModel<SplashViewModel>()

    override fun initInterface(savedInstanceState: Bundle?) {
        observeEvent(screenViewModel.eventLoader, {
            if (it == 1) {
                Handler().postDelayed({ startApp() }, 1000)
            }
        })
    }

    private fun startApp() {
        val newIntent = Intent(this, MainActivity::class.java)
        startActivity(newIntent)
        finish()
    }
}
