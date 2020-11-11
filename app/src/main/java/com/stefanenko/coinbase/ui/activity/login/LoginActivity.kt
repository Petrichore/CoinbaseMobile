package com.stefanenko.coinbase.ui.activity.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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

        viewModel.state.observe(this, {
            when (it) {
                is State.AuthCompleted -> {
                    startActivityInNewTask(MainActivity::class.java)
                }
                is State.ShowErrorMessage -> {
                    showInfoDialog("Error", it.error)
                }
                is State.OpenCoinbaseAuthPage -> {
                    val intent = Intent(Intent.ACTION_VIEW, it.uri)
                    startActivity(intent)
                }
            }
        })

        viewModel.interruptibleState.observe(this, {
            when (it) {
                is InterruptibleState.StartLoading -> {
                    progressBar.visibility = View.VISIBLE
                    shadowView.visibility = View.VISIBLE
                }

                is InterruptibleState.StopLoading -> {
                    progressBar.visibility = View.GONE
                    shadowView.visibility = View.GONE
                }
            }
        })
    }

    private fun setListeners() {
        signInBtn.setOnClickListener {
            viewModel.performAuth()
        }

        quickStartBtn.setOnClickListener {
            startActivity(MainActivity::class.java)
        }
    }

    private fun onIntent(intent: Intent) {
        if (intent.action != null && intent.action == "android.intent.action.VIEW" && intent.data != null) {
            viewModel.completeAuth(intent.data!!)
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