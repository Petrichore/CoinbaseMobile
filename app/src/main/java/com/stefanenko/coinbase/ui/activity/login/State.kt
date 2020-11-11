package com.stefanenko.coinbase.ui.activity.login

import android.net.Uri

sealed class State {
    object AuthCompleted : State()
    data class ShowErrorMessage(val error: String) : State()
    data class OpenCoinbaseAuthPage(val uri: Uri) : State()
}

sealed class InterruptibleState {
    object StartLoading : InterruptibleState()
    object StopLoading : InterruptibleState()
}