package com.stefanenko.coinbase.data.network.api

import com.stefanenko.coinbase.data.network.dto.activeCurrency.ActiveCurrencyResponse
import retrofit2.Response
import retrofit2.http.GET

interface BitmexMarketApi{

    @GET("instrument/active")
    suspend fun getActiveCurrency(): Response<List<ActiveCurrencyResponse>>
}