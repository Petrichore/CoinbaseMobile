package com.stefanenko.coinbase.util.preferences

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("PrivatePropertyName")
@Singleton
class SettingSwitchersPreferences @Inject constructor(private val appContext: Context) {

    private val SWITCHERS_PREF_TAG = "SWITCHERS_PREF_TAG"

    private val NOTIFICATION_SWITCHERS_STATE_KEY = "NOTIFICATION_SWITCHERS_STATE_KEY"
    private val SOUND_SWITCHERS_STATE_KEY = "SOUND_SWITCHERS_STATE_KEY"

    fun saveNotificationSwitcherState(isChecked: Boolean) {
        appContext.getSharedPreferences(SWITCHERS_PREF_TAG, Context.MODE_PRIVATE).edit()
            .putBoolean(NOTIFICATION_SWITCHERS_STATE_KEY, isChecked).apply()
    }

    fun getNotificationSwitcherState() =
        appContext.getSharedPreferences(SWITCHERS_PREF_TAG, Context.MODE_PRIVATE)
            .getBoolean(NOTIFICATION_SWITCHERS_STATE_KEY, false)


    fun saveSoundSwitcherState(isChecked: Boolean) {
        appContext.getSharedPreferences(SWITCHERS_PREF_TAG, Context.MODE_PRIVATE).edit()
            .putBoolean(SOUND_SWITCHERS_STATE_KEY, isChecked).apply()
    }

    fun getSoundSwitcherState() =
        appContext.getSharedPreferences(SWITCHERS_PREF_TAG, Context.MODE_PRIVATE)
            .getBoolean(SOUND_SWITCHERS_STATE_KEY, false)
}