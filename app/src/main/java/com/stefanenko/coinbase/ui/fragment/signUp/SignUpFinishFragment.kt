package com.stefanenko.coinbase.ui.fragment.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.FragmentSignUpFinishBinding
import com.stefanenko.coinbase.databinding.FragmentSignUpStep2Binding
import com.stefanenko.coinbase.databinding.FragmentSignUpStep3Binding
import com.stefanenko.coinbase.ui.activity.signup.SignUpActivity
import com.stefanenko.coinbase.ui.base.BaseFragment

class SignUpFinishFragment : BaseFragment() {

    private var _binding: FragmentSignUpFinishBinding? = null
    private val binding: FragmentSignUpFinishBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defineOnBackPressedCallBack()
    }


//    override fun setUpGeneralUIElements() {
//        with(activity as SignUpActivity) {
//            toolbar.visibility = View.GONE
//            binding.sigUpFinishBtn.setOnClickListener {
//                (activity as SignUpActivity).finish()
//            }
//        }
//    }

    /**
     * Do nothing cause we shouldn't navigate to previous signUp screen
     *
     **/
    private fun defineOnBackPressedCallBack() {
        val onBackPressCallBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressCallBack
        )
    }
}