package com.stefanenko.coinbase.ui.activity.login

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.useCase.AuthUseCases
import com.stefanenko.coinbase.util.exception.ERROR_AUTH_DEFAULT
import com.stefanenko.coinbase.util.exception.ERROR_INTERNET_CONNECTION
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val authUseCases: AuthUseCases,
    private val connectivityManager: NetworkConnectivityManager
) : ViewModel() {

    val state = MutableLiveData<State>()
    val interruptibleState = MutableLiveData<InterruptibleState>()

    fun performAuth() {
        if (connectivityManager.isConnected()) {
            val uri = authUseCases.startAuth()

            state.value = State.OpenCoinbaseAuthPage(uri)
        } else {
            state.value = State.ShowErrorMessage(ERROR_INTERNET_CONNECTION)
        }
    }

    fun completeAuth(uri: Uri) {
        try {
            interruptibleState.value = InterruptibleState.StartLoading

            val authUrl = uri.toString()
            Log.d("URI", authUrl)

            viewModelScope.launch {
                when (val responseState = authUseCases.completeAuth(authUrl)) {
                    is ResponseState.Data -> {
                        authPreferences.setUserAuthState(true)
                        authPreferences.saveAccessToken(responseState.data.first)
                        authPreferences.saveRefreshToken(responseState.data.second)

                        state.value = State.AuthCompleted
                        interruptibleState.value = InterruptibleState.StopLoading
                    }

                    is ResponseState.Error -> {
                        state.value = State.ShowErrorMessage(responseState.error)
                    }
                }
            }
        } catch (e: IllegalArgumentException) {
            state.value = State.ShowErrorMessage(ERROR_AUTH_DEFAULT)
            e.printStackTrace()
        }
    }
}