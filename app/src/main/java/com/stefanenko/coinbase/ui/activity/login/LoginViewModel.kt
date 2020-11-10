package com.stefanenko.coinbase.ui.activity.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.util.LogPrinter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coinbase.Scope
import com.coinbase.network.Callback
import com.coinbase.resources.auth.AccessToken
import com.stefanenko.coinbase.App.Companion.coinbase
import com.stefanenko.coinbase.util.exception.ExceptionMessage
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
            coinbase.beginAuthorization(
                context,
                Uri.parse(redirectUri),
                Scope.Wallet.User.READ,
                Scope.Wallet.Accounts.READ
            )
        } else {
            _authError.value = ExceptionMessage.ERROR_INTERNET_CONNECTION
        }
    }

    fun completeAuth(uri: Uri, baseAuthUrl: String) {

        //TODO async call for access_token

        val authUrl = uri.toString()
        Log.d("URI:::", "$uri")
        Log.d("URL:::", authUrl)

//        try {
//            val authCode = retrieveAuthCode(authUrl, baseAuthUrl)
//        } catch (e: IllegalArgumentException) {
//            _authError.value = ExceptionMessage.ERROR_AUTH
//            e.printStackTrace()
//        }
    }

    private fun retrieveAuthCode(url: String, baseUrl: String): String {
        if (url.contains(baseUrl)) {
            return url.substring(baseUrl.length)
        } else {
            throw IllegalArgumentException("Invalid url in response")
        }
    }
}