package com.stefanenko.coinbase.ui.fragment.signUp

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.activity.signup.SignUpActivity
import com.stefanenko.coinbase.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_sign_up_finish.*
import kotlinx.android.synthetic.main.fragment_sign_up_start.*

class SignUpFinishFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_sign_up_finish

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defineOnBackPressedCallBack()
        with(activity as SignUpActivity) {
            toolbar.visibility = View.GONE
            sigUpFinishBtn.setOnClickListener {
                (activity as SignUpActivity).finish()
            }
        }
    }

    /**
     * Do nothing couse we shouldn't navigate to previous signUp screen
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