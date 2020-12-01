package com.stefanenko.coinbase.ui.fragment.profile.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.base.BaseFragment
import com.stefanenko.coinbase.util.preferences.SettingSwitchersPreferences
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment : BaseFragment() {

    @Inject
    lateinit var switcherPref: SettingSwitchersPreferences

    override fun getLayoutId(): Int = R.layout.fragment_settings

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
        notificationSwitcher.isChecked = switcherPref.getNotificationSwitcherState()
        soundSwitcher.isChecked = switcherPref.getSoundSwitcherState()
    }

    private fun setListeners() {
        notificationSwitcher.setOnCheckedChangeListener { buttonView, isChecked ->
            switcherPref.saveNotificationSwitcherState(isChecked)
        }

        soundSwitcher.setOnCheckedChangeListener { buttonView, isChecked ->
            switcherPref.saveSoundSwitcherState(isChecked)
        }
    }
}