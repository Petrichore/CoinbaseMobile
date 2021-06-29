package com.stefanenko.coinbase.ui.activity.splash

sealed class SplashActivityState {
    data class ShowErrorMessage(val error: String) : SplashActivityState()
    object UserIsAuth : SplashActivityState()
    object OpenLoginActivity: SplashActivityState()
}