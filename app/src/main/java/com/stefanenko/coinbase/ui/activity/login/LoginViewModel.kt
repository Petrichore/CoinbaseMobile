package com.stefanenko.coinbase.ui.activity.login

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefanenko.coinbase.domain.DataRepository
import com.stefanenko.coinbase.domain.ResponseState
import com.stefanenko.coinbase.util.UrlBuilder
import com.stefanenko.coinbase.util.coinbase.*
import com.stefanenko.coinbase.util.exception.ExceptionMessage
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import com.stefanenko.coinbase.util.preferences.ClientPreferences
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val connectivityManager: NetworkConnectivityManager,
    private val authPreferences: AuthPreferences,
    private val clientPreferences: ClientPreferences
) : ViewModel() {

    val state = MutableLiveData<State>()
    val interruptibleState = MutableLiveData<InterruptibleState>()

    fun performAuth() {
        if (connectivityManager.isConnected()) {
            val scopeSting = ScopeBuilder.build(
                Scope.Wallet.User.READ,
                Scope.Wallet.User.EMAIL,
                Scope.Wallet.Accounts.READ,
                Scope.Wallet.Accounts.UPDATE
            )

            val url = UrlBuilder.buildUrl(
                BASE_AUTH_URL,
                Pair(CLIENT_ID_KEY, clientPreferences.getClientId()),
                Pair(REDIRECT_URI_KEY, REDIRECT_URI_VALUE),
                Pair(RESPONSE_TYPE_KEY, RESPONSE_TYPE_VALUE),
                Pair(SCOPE_KEY, scopeSting)
            )

            state.value = State.OpenCoinbaseAuthPage(Uri.parse(url))
        } else {
            state.value =
                State.ShowErrorMessage(ExceptionMessage.ERROR_INTERNET_CONNECTION)
        }
    }

    fun completeAuth(uri: Uri) {
        interruptibleState.value = InterruptibleState.StartLoading


        val authUrl = uri.toString()
        Log.d("URI", authUrl)


        try {
            val authCode = retrieveAuthCode(authUrl, BASE_AUTH_URL)
            Log.d("AUTH CODE:::", authCode)

            viewModelScope.launch {
                val responseState = dataRepository.getAccessToken(
                    clientPreferences.getClientId(),
                    clientPreferences.getClientSecret(),
                    REDIRECT_URI_VALUE,
                    authCode
                )

                when (responseState) {
                    is ResponseState.Data -> {
                        authPreferences.setUserAuthState(true)
                        authPreferences.saveAccessToken(responseState.data.accessToken)
                        authPreferences.saveRefreshToken(responseState.data.refreshToken)

                        state.value = State.AuthCompleted
                        interruptibleState.value = InterruptibleState.StopLoading
                    }

                    is ResponseState.Error -> {
                        state.value = State.ShowErrorMessage(responseState.error)
                    }

                    is ResponseState.InProgress -> {

                    }
                }
            }
        } catch (e: IllegalArgumentException) {
            state.value = State.ShowErrorMessage(ExceptionMessage.ERROR_AUTH)
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