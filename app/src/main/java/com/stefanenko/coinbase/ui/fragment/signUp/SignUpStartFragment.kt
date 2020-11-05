package com.stefanenko.coinbase.ui.fragment.signUp

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_sign_up_start.*

class SignUpStartFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_sign_up_start

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sigUpStartNextBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpStartFragment_to_signUpPersonalInfoFragment)
        }
    }
}