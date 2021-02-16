package com.stefanenko.coinbase.ui.fragment.guestMode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stefanenko.coinbase.databinding.FragmentProfileForGuestsBinding
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.activity.login.LoginActivity
import com.stefanenko.coinbase.ui.base.BaseFragment

class ProfileGuestModeFragment : BaseFragment() {

    private var _binding: FragmentProfileForGuestsBinding? = null
    private val binding: FragmentProfileForGuestsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileForGuestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toAuthBtn.setOnClickListener {
            startActivityInNewTask(LoginActivity::class.java)
        }
    }
}