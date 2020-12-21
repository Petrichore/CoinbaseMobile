package com.stefanenko.coinbase.ui.fragment.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.FragmentSignUpStep2Binding
import com.stefanenko.coinbase.databinding.FragmentSignUpStep3Binding
import com.stefanenko.coinbase.ui.activity.signup.SignUpActivity
import com.stefanenko.coinbase.ui.base.BaseFragment

class SignUpAuthenticationInfoFragment : BaseFragment() {

    private var _binding: FragmentSignUpStep3Binding? = null
    private val binding: FragmentSignUpStep3Binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpStep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as SignUpActivity).toolbar.title =
            resources.getString(R.string.toolbar_title_sign_up_step_3)

        binding.sigUpStep3NextBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpAuthenticationInfoFragment_to_signUpFinishFragment)
        }
    }
}