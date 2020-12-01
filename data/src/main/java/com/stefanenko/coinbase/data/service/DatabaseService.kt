package com.stefanenko.coinbase.data.service

import com.stefanenko.coinbase.data.database.dao.CurrencyDao
import com.stefanenko.coinbase.data.database.entity.ExchangeRateEntity
import com.stefanenko.coinbase.data.database.entity.FavoriteExchangeRatesEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseService @Inject constructor(private val currencyDao: CurrencyDao) {

    suspend fun addExchangeRateToFavorite(favoriteExchangeRatesEntity: FavoriteExchangeRatesEntity) {
        return withContext(Dispatchers.IO) {
            currencyDao.addCurrencyExchangeRateToFavorite(favoriteExchangeRatesEntity)
        }
    }

    suspend fun getExchangeRateList(): List<ExchangeRateEntity> {
        return withContext(Dispatchers.IO) {
            currencyDao.getExchangeRates()
        }
    }

    suspend fun deleteExchangeRateFromFavorite(favoriteExchangeRatesEntity: FavoriteExchangeRatesEntity): Boolean {
        return withContext(Dispatchers.IO) {
            currencyDao.deleteExchangeRateFromFavorite(favoriteExchangeRatesEntity)
            true
        }
    }

    suspend fun getFavorites(): List<ExchangeRateEntity> {
        return withContext(Dispatchers.IO) {
            currencyDao.getFavoritesData()
        }
    }

    suspend fun addExchangeRateList(exchangeRateList: List<ExchangeRateEntity>): Boolean {
        return withContext(Dispatchers.IO) {
            currencyDao.clearExchangeRateTable()
            currencyDao.insertCurrencyExchangeRates(exchangeRateList)
            true
        }
    }
}