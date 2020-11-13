package com.stefanenko.coinbase.data.network.api

import com.stefanenko.coinbase.data.network.dto.token.RequestAccessToken
import com.stefanenko.coinbase.data.network.dto.token.RequestRefreshToken
import com.stefanenko.coinbase.data.network.dto.token.RequestRevokeToken
import com.stefanenko.coinbase.data.network.dto.token.ResponseAccessToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("oauth/token")
    suspend fun getAccessToken(
        @Body requestAccessToken: RequestAccessToken
    ): Response<ResponseAccessToken>

    @POST("oauth/token")
    suspend fun refreshToken(
        @Body requestRefreshToken: RequestRefreshToken
    ): Response<ResponseAccessToken>

    @POST("oauth/revoke")
    suspend fun revokeToken(
        @Body requestRevokeToken: RequestRevokeToken,
        @Header("Authorization") bearerToken: String
    ): Response<Any>
}