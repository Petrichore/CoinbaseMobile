package com.stefanenko.coinbase.data.network.api

import com.stefanenko.coinbase.data.network.dto.DefaultResponse
import com.stefanenko.coinbase.data.network.dto.profile.ResponseProfile
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface CoinbaseProfileApi {

    @GET("user")
    suspend fun getProfile(
        @Header("Authorization") accessToken: String
    ): Response<DefaultResponse<ResponseProfile>>
}