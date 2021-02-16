package com.stefanenko.coinbase.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.FragmentProfileBinding
import com.stefanenko.coinbase.domain.entity.Profile
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.activity.login.LoginActivity
import com.stefanenko.coinbase.ui.base.BaseObserveFragment
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import com.stefanenko.coinbase.ui.fragment.guestMode.ProfileGuestModeFragment
import javax.inject.Inject

class ProfileFragment: BaseObserveFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    lateinit var viewModel: ProfileViewModel

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        viewModel.getProfile()
    }

    override fun initViewModel() {
        print("InitViewModel")
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
    }

    override fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner, {
            when (it) {
                is StateProfile.ShowProfileData -> {
                    initProfileData(it.profile)
                }
                is StateProfile.LogOut -> {
                    startActivityInNewTask(LoginActivity::class.java)
                }
                is StateProfile.ShowErrorMessage -> {
                    showInfoDialog("Error", it.error)
                }

                StateProfile.GuestMode -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, ProfileGuestModeFragment()).commit()
                }

                StateProfile.StopLoading -> {
                    //TODO dismiss skeleton loading
                }

                StateProfile.StartLoading -> {
                    //TODO implement skeleton loading
                }

                StateProfile.ReAuthPerformed -> {
                    showAlertDialog(
                        "Authorized",
                        "Re-Authorized successfully, update data?",
                        { dialog ->
                            viewModel.getProfile()
                            dialog.dismiss()
                        },
                        { dialog ->
                            //TODO show error screen, that profile data should be loaded
                            dialog.dismiss()
                        })
                }
            }
        })
    }


    private fun setListeners() {
        binding.logOutBtn.setOnClickListener {
            showAlertDialog("Log out", "Do you want to log out?", { dialog ->
                viewModel.performLogout()
                dialog.dismiss()
            }, { dialog ->
                dialog.dismiss()
            })
        }

        binding.settingsCard.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_settingsFragment)
        }
    }

    private fun initProfileData(profile: Profile) {
        binding.userNameText.text = profile.name
        binding.userEmailText.text = profile.email

        Glide.with(requireContext()).load(profile.imageUrl).circleCrop().into(binding.userImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setBlankState()
    }
}