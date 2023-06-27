package com.stefanenko.coinbase.util.errorHandler

import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.useCase.AuthUseCases
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelErrorHandler @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val authPreferences: AuthPreferences
) {

    suspend fun handleUnauthorizedError(
        onTokenRefreshed: (Pair<String, String>) -> Unit,
        onTokenError: (error: String) -> Unit
    ) {
        val refreshToken = authPreferences.getRefreshToken()
        val responseState = authUseCases.refreshToken(refreshToken)

        when (responseState) {
            is ResponseState.Data -> {
                onTokenRefreshed.invoke(responseState.data)
            }
            is ResponseState.Error -> {
                onTokenError.invoke(responseState.error)
            }
            else -> Unit
        }
    }
}