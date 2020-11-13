package com.stefanenko.coinbase.domain.tokenManager

import android.net.Uri

interface TokenManager {
    fun startAuth(redirectUri: String, vararg scopes: String): Uri
    fun completeAuth(authCode: String): String
}