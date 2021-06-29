package com.stefanenko.coinbase.ui.activity.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val authPref: AuthPreferences
) : ViewModel() {

    val state = MutableLiveData<SplashActivityState>()

    fun checkUserAuth(){
        if (authPref.isUserAuth()) {
            state.value = SplashActivityState.UserIsAuth
        } else {
            state.value = SplashActivityState.OpenLoginActivity
        }
        Log.d("Access token:::", authPref.getAccessToken())
        Log.d("Refresh token:::", authPref.getRefreshToken())
    }
}