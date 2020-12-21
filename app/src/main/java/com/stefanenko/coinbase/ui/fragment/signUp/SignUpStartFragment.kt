package com.stefanenko.coinbase.ui.fragment.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.FragmentSignUpStartBinding
import com.stefanenko.coinbase.databinding.FragmentSignUpStep1Binding
import com.stefanenko.coinbase.ui.base.BaseFragment

class SignUpStartFragment : BaseFragment() {
    private var _binding: FragmentSignUpStartBinding? = null
    private val binding: FragmentSignUpStartBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sigUpStartNextBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpStartFragment_to_signUpPersonalInfoFragment)
        }
    }
}