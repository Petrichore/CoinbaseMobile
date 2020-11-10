package com.stefanenko.coinbase.ui.activity.login

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.LogPrinter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coinbase.Scope
import com.stefanenko.coinbase.App.Companion.coinbase
import com.stefanenko.coinbase.util.exception.ExceptionMessage
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import java.lang.IllegalArgumentException
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val networkConnectivity: NetworkConnectivityManager,
    private val authPreferences: AuthPreferences
) : ViewModel() {

    private val _authCompleted = MutableLiveData<Boolean>()
    val authCompleted: LiveData<Boolean>
        get() = _authCompleted

    private val _authError = MutableLiveData<String>()
    val authError: LiveData<String>
        get() = _authError

    fun performAuth(context: Context, redirectUri: String) {
        if (networkConnectivity.isConnected()) {
            coinbase.beginAuthorization(context, Uri.parse(redirectUri), Scope.Wallet.User.READ)
        } else {
            _authError.value = ExceptionMessage.ERROR_INTERNET_CONNECTION
        }
    }

    fun completeAuth(uri: Uri, baseAuthUrl: String) {
        val authUrl = uri.toString()
        try {
            val token = retrieveToken(authUrl, baseAuthUrl)
            authPreferences.saveAuthToken(token)
            authPreferences.setUserAuthState(true)
            _authCompleted.value = true
        } catch (e: IllegalArgumentException) {
            _authError.value = ExceptionMessage.ERROR_AUTH
            e.printStackTrace()
        }
    }

    private fun retrieveToken(url: String, baseUrl: String): String {
        if (url.contains(baseUrl)) {
            return url.substring(baseUrl.length)
        } else {
            throw IllegalArgumentException("Invalid url in response")
        }
    }
}