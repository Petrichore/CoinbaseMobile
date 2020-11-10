package com.stefanenko.coinbase.ui.activity.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.base.BaseActivity
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: LoginViewModel

    override fun getLayoutId(): Int = R.layout.activity_login

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setListeners()
        initViewModel()
        observeViewModel()

        if (intent != null) {
            onIntent(intent)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    private fun observeViewModel() {
        viewModel.authCompleted.observe(this, Observer {
            if (it) {
                startActivityInNewTask(MainActivity::class.java)
            }
        })

        viewModel.authError.observe(this, Observer {
            showInfoDialog("Error", it)
        })
    }

    private fun setListeners() {
        signInBtn.setOnClickListener {
            viewModel.performAuth(this, resources.getString(R.string.redirect_uri))
        }

        quickStartBtn.setOnClickListener {
            startActivity(MainActivity::class.java)
        }
    }

    private fun onIntent(intent: Intent) {
        if (intent.action != null && intent.action == "android.intent.action.VIEW" && intent.data != null) {
            viewModel.completeAuth(intent.data!!, resources.getString(R.string.base_auth_url))
        }
    }

    override fun onNewIntent(intent: Intent?) {
        showDebugLog("onNewIntent")
        if (intent != null) {
            Log.d("Intent", "$intent")
        }
        super.onNewIntent(intent)
    }
}