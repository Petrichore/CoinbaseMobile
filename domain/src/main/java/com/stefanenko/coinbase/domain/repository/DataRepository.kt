package com.stefanenko.coinbase.domain.repository

import android.util.Log
import com.stefanenko.coinbase.domain.exception.ExceptionMapper
import com.stefanenko.coinbase.data.service.DatabaseService
import com.stefanenko.coinbase.data.service.RemoteDataService
import com.stefanenko.coinbase.domain.entity.ActiveCurrency
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.Profile
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.exception.ERROR_EMPTY_LOCAL_STORAGE
import com.stefanenko.coinbase.domain.map.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(
    private val remoteDataService: RemoteDataService,
    private val databaseService: DatabaseService
) {
    internal suspend fun getExchangeRates(baseCurrency: String): ResponseState<List<ExchangeRate>> {
        try {
            val exchangeRateEntityList = databaseService.getExchangeRateList()
            if (exchangeRateEntityList.isNotEmpty()) {
                return ResponseState.Data(exchangeRateEntityList.map { it.mapToExchangeRate() })
            } else {
               return getExchangeRatesRemote(baseCurrency)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseState.Error(ExceptionMapper.mapToAppError(e.message ?: ""))
        }
    }

    internal suspend fun updateExchangeRates(baseCurrency: String): ResponseState<List<ExchangeRate>> {
        try {
            return getExchangeRatesRemote(baseCurrency)
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

    suspend fun getActiveCurrency(): ResponseState<List<ActiveCurrency>> {
        try {
            val responseActiveCurrencyList = remoteDataService.getActiveCurrency()
            val activeCurrencyList = responseActiveCurrencyList.map { ActiveCurrency(it.name) }
            return ResponseState.Data(activeCurrencyList)
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseState.Error(ExceptionMapper.mapToAppError(e.message ?: ""))
        }
    }

    internal suspend fun addExchangeRateToFavorites(exchangeRate: ExchangeRate): ResponseState<Boolean> {
        try {
            databaseService.addExchangeRateToFavorite(exchangeRate.mapToFavoriteExchangeRateEntity())
            return ResponseState.Data(true)
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO add exception message
            return ResponseState.Error("")
        }
    }

    internal suspend fun deleteExchangeRateFromFavorites(exchangeRate: ExchangeRate): ResponseState<Boolean> {
        return try {
            databaseService.deleteExchangeRateFromFavorite(exchangeRate.mapToFavoriteExchangeRateEntity())
            ResponseState.Data(true)
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO add exception message
            return ResponseState.Error("")
        }
    }

    internal suspend fun getFavorites(): ResponseState<List<ExchangeRate>> {
        try {
            val exchangeRateList =
                databaseService.getFavorites().map { it.mapToExchangeRate() }.reversed()
            return ResponseState.Data(exchangeRateList)
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO add exception message
            return ResponseState.Error("")
        }
    }

    private suspend fun getExchangeRatesRemote(baseCurrency: String): ResponseState<List<ExchangeRate>> {
        try {
            val responseExchangeRate = remoteDataService.getExchangeRates(baseCurrency)
            val exchangeRateList = responseExchangeRate.mapToExchangeRates(baseCurrency)
            Log.d("ExchangeRates REMOTE:::", "${exchangeRateList.map { it.mapToExchangeRateEntity() }}")
            databaseService.addExchangeRateList(exchangeRateList.map { it.mapToExchangeRateEntity() })

            return ResponseState.Data(exchangeRateList)
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseState.Error(ExceptionMapper.mapToAppError(e.message ?: ""))
        }
    }

    internal suspend fun getCasedExchangeRates(): ResponseState<List<ExchangeRate>> {
        try {
            val exchangeRateEntityList = databaseService.getExchangeRateList()
            return ResponseState.Data(exchangeRateEntityList.map { it.mapToExchangeRate() })
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseState.Error(ExceptionMapper.mapToAppError(e.message ?: ""))
        }
    }
}