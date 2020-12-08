package com.stefanenko.coinbase.ui.activity.splash

import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.activity.login.LoginActivity
import com.stefanenko.coinbase.ui.base.BaseActivity
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: SplashViewModel

    override fun getLayoutId(): Int = R.layout.fragment_splashscreen

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        initViewModel()
        setObservers()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]
    }

    private fun setObservers() {
        viewModel.scatteringState.observe(this, {
            when (it) {
                is SplashViewModel.ScatteringSplashState.ShowErrorMessage -> showInfoDialog(
                    "Error",
                    it.error
                )
                SplashViewModel.ScatteringSplashState.UserIsAuth -> {
                    showDebugLog("START MAIN")
                    startActivityInNewTask(MainActivity::class.java)
                }
                SplashViewModel.ScatteringSplashState.OpenLoginActivity -> startActivity(
                    LoginActivity::class.java,
                    true
                )
            }
        })
    }

    override fun onResume() {
        super.onResume()
        navigateToNextScreen()
    }

    private fun navigateToNextScreen() {
        Handler().postDelayed({
            viewModel.checkUserAuth()
        }, 500)
    }
}