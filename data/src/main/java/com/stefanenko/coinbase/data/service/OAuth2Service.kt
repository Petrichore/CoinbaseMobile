package com.stefanenko.coinbase.data.service

import com.stefanenko.coinbase.data.network.api.AuthApi
import com.stefanenko.coinbase.data.network.dto.token.RequestAccessToken
import com.stefanenko.coinbase.data.network.dto.token.RequestRefreshToken
import com.stefanenko.coinbase.data.network.dto.token.RequestRevokeToken
import com.stefanenko.coinbase.data.network.dto.token.ResponseAccessToken
import com.stefanenko.coinbase.data.util.NetworkResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OAuth2Service @Inject constructor(retrofitService: RetrofitService) {

    private val authApi = retrofitService.createAuthService(AuthApi::class.java)

    suspend fun getAccessToken(requestAccessToken: RequestAccessToken): ResponseAccessToken {
        return withContext(Dispatchers.IO) {
            val response = authApi.getAccessToken(requestAccessToken)
            try {
                NetworkResponseHandler.handleResponse(response)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun refreshToken(requestRefreshToken: RequestRefreshToken): ResponseAccessToken {
        return withContext(Dispatchers.IO) {
            val response = authApi.refreshToken(requestRefreshToken)
            try {
                NetworkResponseHandler.handleResponse(response)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun revokeToken(requestRevokeToken: RequestRevokeToken, bearerToken: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = authApi.revokeToken(requestRevokeToken, bearerToken)
            try {
                NetworkResponseHandler.handleResponse(response)
                true
            } catch (e: Exception) {
                throw e
            }
        }
    }
}