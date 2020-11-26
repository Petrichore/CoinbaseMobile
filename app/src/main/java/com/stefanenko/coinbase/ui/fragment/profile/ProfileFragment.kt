package com.stefanenko.coinbase.ui.fragment.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.domain.entity.Profile
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.activity.login.LoginActivity
import com.stefanenko.coinbase.ui.base.BaseObserveFragment
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import com.stefanenko.coinbase.ui.fragment.guestMode.FavoritesGuestModeFragment
import com.stefanenko.coinbase.ui.fragment.guestMode.ProfileGuestModeFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileFragment : BaseObserveFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ProfileViewModel

    override fun getLayoutId(): Int = R.layout.fragment_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        (activity as MainActivity).toolbar.title =
            resources.getString(R.string.toolbar_title_profile)
        viewModel.getProfile()
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
    }

    override fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner, {
            when (it) {
                is StateProfile.ShowProfileData -> {
                    initProfileData(it.profile)
                }
                is StateProfile.LogOut -> {
                    (activity as MainActivity).startActivityInNewTask(LoginActivity::class.java)
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
        viewModel.stateScattering.observe(viewLifecycleOwner, { stateScattering ->
            when (stateScattering) {
                is StateScattering.ShowErrorMessage -> {
                    showInfoDialog("Error", stateScattering.error)
                }
            }
        })
    }

    private fun setListeners() {
        logOutBtn.setOnClickListener {
            showAlertDialog("Log out", "Do you want to log out?", { dialog ->
                viewModel.performLogout()
                dialog.dismiss()
            }, { dialog ->
                dialog.dismiss()
            })
        }
    }

    private fun initProfileData(profile: Profile) {
        userNameText.text = profile.name
        userEmailText.text = profile.email

        Glide.with(requireContext()).load(profile.imageUrl).circleCrop().into(userImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.scatterStates()
    }
}