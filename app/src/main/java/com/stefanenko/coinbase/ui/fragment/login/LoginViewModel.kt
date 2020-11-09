package com.stefanenko.coinbase.ui.fragment.login

import android.content.Context
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {

    // Must be the same as entered into coinbase application
    private val redirectUri = "my-example-app://coinbase-oauth"
    private val clientId = "979fbe1bf76735a1179d82729ed941ffdd5331686891c10c12d1b25f24ac5293"
    private val clientSecret = "208dfff66f3af0ad840541b2dc41f417ce785f187913acb6837bdb39e338e207"

    @Inject
    lateinit var appContext: Context

    fun performOAuth() {
        //OAuth.beginAuthorization(appContext, clientId, "user", redirectUri, null)
    }
}