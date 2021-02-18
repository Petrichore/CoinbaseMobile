package com.stefanenko.coinbase.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.ActivityLoginBinding
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
                is StateLogin.AuthCompleted -> {
                    startActivityInNewTask(MainActivity::class.java)
                }
                is StateLogin.ShowErrorMessage -> {
                    showInfoDialog(resources.getString(R.string.alert_dialog_title_error), it.error)
                }
                is StateLogin.OpenCoinbaseAuthPage -> {
                    val intent = Intent(Intent.ACTION_VIEW, it.uri).apply {
                        addCategory(Intent.CATEGORY_BROWSABLE)
                    }
                    startActivity(intent)
                }
                StateLogin.StartLoading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.shadowView.visibility = View.VISIBLE
                }
                StateLogin.StopLoading -> {
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
}