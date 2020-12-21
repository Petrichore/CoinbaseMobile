package com.stefanenko.coinbase.ui.activity.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.ActivityLoginBinding
import com.stefanenko.coinbase.databinding.ActivityMainBinding
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.base.BaseActivity
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import dagger.android.AndroidInjection
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: LoginViewModel

    lateinit var binding: ActivityLoginBinding

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                    binding.progressBar.visibility = View.VISIBLE
                    binding.shadowView.visibility = View.VISIBLE
                }

                is InterruptibleState.StopLoading -> {
                    binding.progressBar.visibility = View.GONE
                    binding.shadowView.visibility = View.GONE
                }
            }
        })
    }

    private fun setListeners() {
        binding.signInBtn.setOnClickListener {
            viewModel.performAuth()
        }

        binding.quickStartBtn.setOnClickListener {
            startActivity(MainActivity::class.java, false)
        }
    }

    private fun onIntent(intent: Intent) {
        if (intent.action != null && intent.action == Intent.ACTION_VIEW && intent.data != null) {
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