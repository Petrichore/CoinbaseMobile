package com.stefanenko.coinbase.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.activity.login.LoginActivity
import com.stefanenko.coinbase.ui.base.BaseActivity
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : BaseActivity() {
    @Inject
    lateinit var authPref: AuthPreferences

    override fun getLayoutId(): Int = R.layout.fragment_splashscreen

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        navigateToNextScreen()
    }

    private fun navigateToNextScreen() {
        Handler().postDelayed({
            startActivity(LoginActivity::class.java)

//            if (authPref.isUserAuth()) {
//                startActivityInNewTask(MainActivity::class.java)
//            } else {
//                startActivity(LoginActivity::class.java)
//            }
            Log.d("Token:::", authPref.getAuthToken())
            finish()
        }, 500) // TODO change to 3000 millis

    }
}