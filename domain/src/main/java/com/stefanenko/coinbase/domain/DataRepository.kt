package com.stefanenko.coinbase.domain

import com.stefanenko.coinbase.data.network.dto.auth.RequestAccessToken
import com.stefanenko.coinbase.data.service.RemoteDataService
import com.stefanenko.coinbase.domain.entity.AccessToken
import com.stefanenko.coinbase.domain.map.mapToAccessToken
import com.stefanenko.coinbase.domain.util.GRANT_TYPE_ACCESS_TOKEN
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Exception

@Singleton
class DataRepository @Inject constructor(private val remoteDataService: RemoteDataService) {

    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        authCode: String
    ): ResponseState<AccessToken> {
        try {
            val requestAccessToken = RequestAccessToken(
                clientId,
                clientSecret,
                GRANT_TYPE_ACCESS_TOKEN,
                redirectUri,
                authCode
            )
            val responseAccessToken = remoteDataService.getAccessToken(requestAccessToken)

            return ResponseState.Data(responseAccessToken.mapToAccessToken(requestAccessToken))
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseState.Error(e.message ?: "ERROR_MESSAGE_MISSING")
        }
    }
}