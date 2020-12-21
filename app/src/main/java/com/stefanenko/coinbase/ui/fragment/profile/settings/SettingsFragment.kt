package com.stefanenko.coinbase.ui.fragment.profile.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.FragmentSettingsBinding
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.base.BaseFragment
import com.stefanenko.coinbase.util.preferences.SettingSwitchersPreferences
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SettingsFragment : BaseFragment() {

    @Inject
    lateinit var switcherPref: SettingSwitchersPreferences

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).toolbar.title = resources.getString(R.string.toolbar_title_settings)
        setListeners()
        setCurrentSwitchersState()
    }

    private fun setCurrentSwitchersState() {
        binding.notificationSwitcher.isChecked = switcherPref.getNotificationSwitcherState()
        binding.soundSwitcher.isChecked = switcherPref.getSoundSwitcherState()
    }

    private fun setListeners() {
        binding.notificationSwitcher.setOnCheckedChangeListener { buttonView, isChecked ->
            switcherPref.saveNotificationSwitcherState(isChecked)
        }

        binding.soundSwitcher.setOnCheckedChangeListener { buttonView, isChecked ->
            switcherPref.saveSoundSwitcherState(isChecked)
        }
    }
}