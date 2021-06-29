package com.stefanenko.coinbase.ui.activity.login

import android.net.Uri

sealed class StateLogin {
    object AuthCompleted : StateLogin()
    data class ShowErrorMessage(val error: String) : StateLogin()
    data class OpenCoinbaseAuthPage(val uri: Uri) : StateLogin()
    object StartLoading : StateLogin()
    object StopLoading : StateLogin()
}