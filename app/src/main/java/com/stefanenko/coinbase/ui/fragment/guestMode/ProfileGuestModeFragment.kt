package com.stefanenko.coinbase.ui.fragment.guestMode

import android.os.Bundle
import android.view.View
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.activity.login.LoginActivity
import com.stefanenko.coinbase.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_favorites_for_guests.toAuthBtn

class ProfileGuestModeFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_profile_for_guests

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toAuthBtn.setOnClickListener {
            (activity as MainActivity).startActivityInNewTask(LoginActivity::class.java)
        }
    }
}