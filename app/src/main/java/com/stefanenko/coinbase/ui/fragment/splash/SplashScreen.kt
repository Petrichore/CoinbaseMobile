package com.stefanenko.coinbase.ui.fragment.splash

import android.os.Bundle
import android.os.Handler
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.activity.StartActivity
import com.stefanenko.coinbase.ui.base.BaseFragment

class SplashScreen : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_splashscreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToMainScreen()
    }

    private fun navigateToMainScreen() {
        Handler().postDelayed({
            (activity as StartActivity).navController.navigate(R.id.action_splashScreen_to_loginFragment)
        },3000)
    }
}