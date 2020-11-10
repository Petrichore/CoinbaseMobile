package com.stefanenko.coinbase.ui.fragment.signUp

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.activity.signup.SignUpActivity
import com.stefanenko.coinbase.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_sign_up_step_2.*

class SignUpAddressFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_sign_up_step_2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as SignUpActivity).toolbar.title =
            resources.getString(R.string.toolbar_title_sign_up_step_2)

        sigUpStep2NextBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpAddressFragment_to_signUpAuthenticationInfoFragment)
        }

    }
}