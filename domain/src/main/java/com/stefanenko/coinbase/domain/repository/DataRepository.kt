package com.stefanenko.coinbase.domain.repository

import com.stefanenko.coinbase.domain.exception.ExceptionMapper
import com.stefanenko.coinbase.data.service.DatabaseService
import com.stefanenko.coinbase.data.service.RemoteDataService
import com.stefanenko.coinbase.domain.entity.ActiveCurrency
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.Profile
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.util.mapper.Mapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(
    private val remoteDataService: RemoteDataService,
    private val databaseService: DatabaseService,
    private val mapper: Mapper
) {
    internal suspend fun getExchangeRates(baseCurrency: String): ResponseState<List<ExchangeRate>> {
        try {
            val exchangeRateEntityList = databaseService.getExchangeRateList()
            if (exchangeRateEntityList.isNotEmpty()) {
                val exchangeRateList = exchangeRateEntityList.map { mapper.map(it) }
                return ResponseState.Data(exchangeRateList)
            } else {
                val exchangeRateList = getExchangeRatesRemote(baseCurrency)
                updateExchangeRateListInDatabase(exchangeRateList)
                return ResponseState.Data(exchangeRateList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseState.Error(ExceptionMapper.mapToAppError(e.message ?: ""))
        }
    }

    internal suspend fun updateExchangeRates(baseCurrency: String): ResponseState<List<ExchangeRate>> {
        try {
            val exchangeRateList = getExchangeRatesRemote(baseCurrency)
            updateExchangeRateListInDatabase(exchangeRateList)
            return ResponseState.Data(exchangeRateList)
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseState.Error(ExceptionMapper.mapToAppError(e.message ?: ""))
        }
    }

    internal suspend fun getProfile(accessToken: String): ResponseState<Profile> {
        try {
            val responseProfile = remoteDataService.getProfile(accessToken)
            val profile = mapper.map(responseProfile)
            return ResponseState.Data(profile)
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseState.Error(ExceptionMapper.mapToAppError(e.message ?: ""))
        }
    }

    suspend fun getActiveCurrency(): ResponseState<List<ActiveCurrency>> {
        try {
            val responseActiveCurrencyList = remoteDataService.getActiveCurrency()
            val activeCurrencyList = responseActiveCurrencyList.map { mapper.map(it) }
            return ResponseState.Data(activeCurrencyList)
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseState.Error(ExceptionMapper.mapToAppError(e.message ?: ""))
        }
    }

    internal suspend fun addExchangeRateToFavorite(exchangeRate: ExchangeRate): ResponseState<Boolean> {
        val isAddedSuccessfully =
            databaseService.addExchangeRateToFavorite(
                mapper.mapToFavoriteExchangeRateEntity(
                    exchangeRate
                )
            )
        if (isAddedSuccessfully) {
            return ResponseState.Data(true)
        } else {
            return ResponseState.Error("")
        }
    }

    internal suspend fun deleteExchangeRateFromFavorites(exchangeRate: ExchangeRate): ResponseState<Boolean> {
        val isSuccesses =
            databaseService.deleteExchangeRateFromFavorite(
                mapper.mapToFavoriteExchangeRateEntity(
                    exchangeRate
                )
            )
        if (isSuccesses) {
            return ResponseState.Data(true)
        } else {
            //TODO add exception message
            return ResponseState.Error("")
        }
    }

    internal suspend fun getFavorites(): ResponseState<List<ExchangeRate>> {
        try {
            val exchangeRateList = databaseService.getFavorites().map { mapper.map(it) }.reversed()
            return ResponseState.Data(exchangeRateList)
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO add exception message
            return ResponseState.Error("")
        }
    }

    private suspend fun getExchangeRatesRemote(baseCurrency: String): List<ExchangeRate> {
        try {
            val responseExchangeRate = remoteDataService.getExchangeRates(baseCurrency)
            return mapper.map(responseExchangeRate)
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun updateExchangeRateListInDatabase(exchangeRateList: List<ExchangeRate>): ResponseState<Boolean> {
        try {
            val exchangeRateEntityList = exchangeRateList.map { mapper.mapToExchangeRateEntity(it) }
            val isUpdatedSuccessfully =
                databaseService.updateExchangeRateList(exchangeRateEntityList)
            return ResponseState.Data(isUpdatedSuccessfully)
        } catch (e: Exception) {
            throw e
        }
    }

    internal suspend fun getCashedExchangeRates(): ResponseState<List<ExchangeRate>> {
        try {
            val exchangeRateEntityList = databaseService.getExchangeRateList()
            return ResponseState.Data(exchangeRateEntityList.map { mapper.map(it) })
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseState.Error(ExceptionMapper.mapToAppError(e.message ?: ""))
        }
    }
}