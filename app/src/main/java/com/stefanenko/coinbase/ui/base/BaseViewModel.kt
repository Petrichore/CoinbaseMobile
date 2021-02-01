package com.stefanenko.coinbase.ui.base

import androidx.lifecycle.ViewModel
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.useCase.AuthUseCases
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var authUseCases: AuthUseCases

    protected suspend fun handleTokenRefresh(
        refreshToken: String,
        onTokenRefreshed: (Pair<String, String>) -> Unit,
        onTokenError: (error: String) -> Unit
    ) {
        when (val responseState = authUseCases.refreshToken(refreshToken)) {
            is ResponseState.Data -> {
                onTokenRefreshed.invoke(responseState.data)
            }
            is ResponseState.Error -> {
                onTokenError.invoke(responseState.error)
            }
        }
    }
}