package com.stefanenko.coinbase.ui.activity.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stefanenko.coinbase.util.deppLink.DeepLinkParser
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val authPref: AuthPreferences
) : ViewModel() {

    val scatteringState = MutableLiveData<ScatteringSplashState>()

    fun checkUserAuth(){
        if (authPref.isUserAuth()) {
            scatteringState.value = ScatteringSplashState.UserIsAuth
        } else {
            scatteringState.value = ScatteringSplashState.OpenLoginActivity
        }
        Log.d("Access token:::", authPref.getAccessToken())
        Log.d("Refresh token:::", authPref.getRefreshToken())
    }

    sealed class ScatteringSplashState {
        data class ShowErrorMessage(val error: String) : ScatteringSplashState()
        object UserIsAuth : ScatteringSplashState()
        object OpenLoginActivity: ScatteringSplashState()
        object Scatter: ScatteringSplashState()
    }
}