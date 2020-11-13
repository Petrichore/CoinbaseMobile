package com.stefanenko.coinbase.ui.base

import androidx.lifecycle.ViewModel
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.tokenManager.AuthTokenManager
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var tokenManager: AuthTokenManager

    protected suspend fun handleTokenRefresh(
        refreshToken: String,
        onTokenRefreshed: (Pair<String, String>) -> Unit,
        onTokenError: (error: String) -> Unit
    ) {
        val responseState = tokenManager.refreshToken(refreshToken)
        when (responseState) {
            is ResponseState.Data -> {
                onTokenRefreshed.invoke(responseState.data)
            }
            is ResponseState.Error -> {
                onTokenError.invoke(responseState.error)
            }
        }
    }
}