package com.stefanenko.coinbase.domain.repository

import com.stefanenko.coinbase.domain.exception.ExceptionMapper
import com.stefanenko.coinbase.data.network.dto.DefaultMarketRequest
import com.stefanenko.coinbase.data.service.RemoteDataService
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.Profile
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.map.mapToExchangeRates
import com.stefanenko.coinbase.domain.map.mapToProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val remoteDataService: RemoteDataService) {

    suspend fun getProfile(
        accessToken: String,
        refreshToken: String,
        currency: String
    ): ResponseState<List<ExchangeRate>> {
        try {
            val defaultRequest = DefaultMarketRequest(accessToken, refreshToken, currency)
            val responseExchangeRate = remoteDataService.getExchangeRates(defaultRequest)
            return ResponseState.Data(responseExchangeRate.mapToExchangeRates())
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseState.Error(ExceptionMapper.mapToAppError(e.message ?: ""))
        }
    }

    suspend fun getProfile(accessToken: String): ResponseState<Profile> {
        try {
            val responseProfile = remoteDataService.getProfile(accessToken)
            return ResponseState.Data(responseProfile.mapToProfile())
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseState.Error(ExceptionMapper.mapToAppError(e.message ?: ""))
        }
    }


}