package com.stefanenko.coinbase.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.activity.app.MainActivity
import com.stefanenko.coinbase.ui.activity.signup.SignUpActivity
import com.stefanenko.coinbase.ui.base.BaseObserveFragment
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : BaseObserveFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_login

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: LoginViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    override fun initObservers() {}

    private fun setListeners() {
        signInBtn.setOnClickListener {
            viewModel.performOAuth()
        }

        createAccountBtn.setOnClickListener {
            startActivity(Intent(requireActivity(), SignUpActivity::class.java))
        }

        quickStartBtn.setOnClickListener {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
        }
    }
}