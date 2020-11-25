package com.stefanenko.coinbase.data.network.api

import com.stefanenko.coinbase.data.network.dto.DefaultResponse
import com.stefanenko.coinbase.data.network.dto.exchange.ResponseExchangerRates
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarketApi {

    @GET("exchange-rates")
    suspend fun getExchangeRates(
        @Query("currency") currency: String
    ): Response<DefaultResponse<ResponseExchangerRates>>
}