package com.stefanenko.coinbase.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.activity.signup.SignUpActivity
import com.stefanenko.coinbase.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        createAccountBtn.setOnClickListener {
            startActivity(Intent(requireActivity(), SignUpActivity::class.java))
        }
    }
}