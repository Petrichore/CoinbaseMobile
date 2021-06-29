package com.stefanenko.coinbase.data.service

import com.stefanenko.coinbase.data.network.api.BitmexMarketApi
import com.stefanenko.coinbase.data.network.api.CoinbaseMarketApi
import com.stefanenko.coinbase.data.network.api.CoinbaseProfileApi
import com.stefanenko.coinbase.data.network.dto.activeCurrency.ActiveCurrencyResponse
import com.stefanenko.coinbase.data.network.dto.exchange.ResponseExchangerRates
import com.stefanenko.coinbase.data.network.dto.profile.ResponseProfile
import com.stefanenko.coinbase.data.util.NetworkResponseHandler
import com.stefanenko.coinbase.data.util.coroutineDispatcher.BaseCoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataService @Inject constructor(
    private val marketApi: CoinbaseMarketApi,
    private val coinbaseProfileApi: CoinbaseProfileApi,
    private val bitmexApi: BitmexMarketApi,
    private val responseHandler: NetworkResponseHandler,
    private val dispatcher: BaseCoroutineDispatcher
) {

    suspend fun getExchangeRates(baseCurrency: String): ResponseExchangerRates {
        return withContext(dispatcher.io()) {
            try {
                val response = marketApi.getExchangeRatesRequest(baseCurrency)
                val defaultResponse = responseHandler.handleResponse(response)
                defaultResponse.data
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getProfile(accessToken: String): ResponseProfile {
        return withContext(dispatcher.io()) {
            try {
                val response = coinbaseProfileApi.getProfile(accessToken)
                val defaultResponse = responseHandler.handleResponse(response)
                defaultResponse.data
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getActiveCurrency(): List<ActiveCurrencyResponse> {
        return withContext(dispatcher.io()) {
            try {
                val response = bitmexApi.getActiveCurrency()
                val responseData = responseHandler.handleResponse(response)
                responseData
            } catch (e: Exception) {
                throw e
            }
        }
    }
}
