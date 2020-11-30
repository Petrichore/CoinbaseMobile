package com.stefanenko.coinbase.data.service

import com.stefanenko.coinbase.data.network.api.BitmexMarketApi
import com.stefanenko.coinbase.data.network.api.CoinbaseMarketApi
import com.stefanenko.coinbase.data.network.api.ProfileApi
import com.stefanenko.coinbase.data.network.dto.activeCurrency.ActiveCurrencyResponse
import com.stefanenko.coinbase.data.network.dto.exchange.ResponseExchangerRates
import com.stefanenko.coinbase.data.network.dto.profile.ResponseProfile
import com.stefanenko.coinbase.data.util.NetworkResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataService @Inject constructor(retrofitService: RetrofitService) {

    private val marketApi = retrofitService.createCoinbaseService(CoinbaseMarketApi::class.java)
    private val profileApi = retrofitService.createCoinbaseService(ProfileApi::class.java)
    private val bitmexApi = retrofitService.createBitmexService(BitmexMarketApi::class.java)

    suspend fun getExchangeRates(baseCurrency: String): ResponseExchangerRates {
        return withContext(Dispatchers.IO) {
            try {
                val response = marketApi.getExchangeRates(baseCurrency)
                val defaultResponse = NetworkResponseHandler.handleResponse(response)
                defaultResponse.data
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getProfile(accessToken: String): ResponseProfile {
        return withContext(Dispatchers.IO) {
            try {
                val response = profileApi.getProfile(accessToken)
                val defaultResponse = NetworkResponseHandler.handleResponse(response)
                defaultResponse.data
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getActiveCurrency(): List<ActiveCurrencyResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = bitmexApi.getActiveCurrency()
                val responseData = NetworkResponseHandler.handleResponse(response)
                responseData
            } catch (e: Exception) {
                throw e
            }
        }
    }
}
