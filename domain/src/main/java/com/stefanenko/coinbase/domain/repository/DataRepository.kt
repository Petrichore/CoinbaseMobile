package com.stefanenko.coinbase.domain.repository

import com.stefanenko.coinbase.domain.exception.ExceptionMapper
import com.stefanenko.coinbase.data.service.DatabaseService
import com.stefanenko.coinbase.data.service.RemoteDataService
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.Profile
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.map.mapToExchangeRateEntity
import com.stefanenko.coinbase.domain.map.mapToExchangeRates
import com.stefanenko.coinbase.domain.map.mapToProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(
    private val remoteDataService: RemoteDataService,
    private val databaseService: DatabaseService
) {
    internal suspend fun getCurrenciesExchangeRates(baseCurrency: String): ResponseState<List<ExchangeRate>> {
        try {
            val responseExchangeRate = remoteDataService.getExchangeRates(baseCurrency)
            return ResponseState.Data(responseExchangeRate.mapToExchangeRates(baseCurrency))
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseState.Error(ExceptionMapper.mapToAppError(e.message ?: ""))
        }
    }

    internal suspend fun getProfile(accessToken: String): ResponseState<Profile> {
        try {
            val responseProfile = remoteDataService.getProfile(accessToken)
            return ResponseState.Data(responseProfile.mapToProfile())
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseState.Error(ExceptionMapper.mapToAppError(e.message ?: ""))
        }
    }

    internal suspend fun addExchangeRateToFavorite(exchangeRate: ExchangeRate): ResponseState<Boolean> {
        try {
            databaseService.addExchangeRate(exchangeRate.mapToExchangeRateEntity())
            return ResponseState.Data(true)
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO add exception message
            return ResponseState.Error("")
        }
    }

    internal suspend fun deleteExchangeRateFromFavorites(exchangeRate: ExchangeRate): ResponseState<Boolean> {
        return try {
            databaseService.deleteExchangeRate(exchangeRate.mapToExchangeRateEntity())
            ResponseState.Data(true)
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO add exception message
            return ResponseState.Error("")
        }
    }

    internal suspend fun getFavorites(): ResponseState<List<ExchangeRate>> {
        try {
            val exchangeRateList = databaseService.getFavorites()
                .map {
                    ExchangeRate(
                        it.baseCurrencyName,
                        it.currencyName,
                        it.exchangeRate,
                        it.addDate,
                        it.addTime,
                        it.currencyId
                    )
                }
            return ResponseState.Data(exchangeRateList)
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO add exception message
            return ResponseState.Error("")
        }
    }
}