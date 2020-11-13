package com.stefanenko.coinbase.ui.activity.login

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.tokenManager.AuthTokenManager
import com.stefanenko.coinbase.domain.tokenManager.BASE_AUTH_URL
import com.stefanenko.coinbase.domain.tokenManager.REDIRECT_URI_VALUE
import com.stefanenko.coinbase.domain.tokenManager.scope.Scope
import com.stefanenko.coinbase.util.exception.ERROR_AUTH_DEFAULT
import com.stefanenko.coinbase.util.exception.ERROR_INTERNET_CONNECTION
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val authTokenManager: AuthTokenManager,
    private val connectivityManager: NetworkConnectivityManager
) : ViewModel() {

    val state = MutableLiveData<State>()
    val interruptibleState = MutableLiveData<InterruptibleState>()

    fun performAuth() {
        if(connectivityManager.isConnected()){
            val uri = authTokenManager.startAuth(
                REDIRECT_URI_VALUE,
                Scope.Wallet.User.READ,
                Scope.Wallet.User.EMAIL,
                Scope.Wallet.Accounts.READ,
                Scope.Wallet.Accounts.UPDATE
            )

            state.value = State.OpenCoinbaseAuthPage(uri)
        }else{
            state.value = State.ShowErrorMessage(ERROR_INTERNET_CONNECTION)
        }
    }

    fun completeAuth(uri: Uri) {
        try {
            interruptibleState.value = InterruptibleState.StartLoading

            val authUrl = uri.toString()
            Log.d("URI", authUrl)

            val authCode = retrieveAuthCode(authUrl, BASE_AUTH_URL)
            Log.d("AUTH CODE:::", authCode)

            viewModelScope.launch {
                when (val responseState = authTokenManager.completeAuth(authCode)) {
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

    private fun retrieveAuthCode(url: String, baseUrl: String): String {
        if (url.contains(baseUrl)) {
            return url.substring(baseUrl.length + 1)
        } else {
            throw IllegalArgumentException("Invalid url in response")
        }
    }
}