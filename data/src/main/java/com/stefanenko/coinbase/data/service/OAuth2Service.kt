package com.stefanenko.coinbase.data.service

import com.stefanenko.coinbase.data.network.api.AuthApi
import com.stefanenko.coinbase.data.network.dto.token.RequestAccessToken
import com.stefanenko.coinbase.data.network.dto.token.RequestRefreshToken
import com.stefanenko.coinbase.data.network.dto.token.RequestRevokeToken
import com.stefanenko.coinbase.data.network.dto.token.ResponseAccessToken
import com.stefanenko.coinbase.data.util.NetworkResponseHandler
import com.stefanenko.coinbase.data.util.coroutineDispatcher.BaseCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.PrivateKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OAuth2Service @Inject constructor(
    private val authApi: AuthApi,
    private val responseHandler: NetworkResponseHandler,
    private val dispatcher: BaseCoroutineDispatcher
) {

    suspend fun getAccessToken(requestAccessToken: RequestAccessToken): ResponseAccessToken {
        return withContext(dispatcher.io()) {
            try {
                val response = authApi.getAccessToken(requestAccessToken)
                responseHandler.handleResponse(response)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun refreshToken(requestRefreshToken: RequestRefreshToken): ResponseAccessToken {
        return withContext(dispatcher.io()) {
            try {
                val response = authApi.refreshToken(requestRefreshToken)
                responseHandler.handleResponse(response)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun revokeToken(requestRevokeToken: RequestRevokeToken, bearerToken: String): Boolean {
        return withContext(dispatcher.io()) {
            try {
                val response = authApi.revokeToken(requestRevokeToken, bearerToken)
                responseHandler.handleResponse(response)
                true
            } catch (e: Exception) {
                throw e
            }
        }
    }
}