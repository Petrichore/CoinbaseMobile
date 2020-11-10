package com.stefanenko.coinbase.data.network.api

import com.stefanenko.coinbase.data.network.dto.auth.RequestAccessToken
import com.stefanenko.coinbase.data.network.dto.auth.ResponseAccessToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("oauth/token")
    suspend fun getAccessToken(@Body requestAccessToken: RequestAccessToken): Response<ResponseAccessToken>
}