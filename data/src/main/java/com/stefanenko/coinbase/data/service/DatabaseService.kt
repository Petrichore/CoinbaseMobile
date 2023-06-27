package com.stefanenko.coinbase.data.service

import com.stefanenko.coinbase.data.database.dao.CurrencyDao
import com.stefanenko.coinbase.data.database.entity.ExchangeRateEntity
import com.stefanenko.coinbase.data.database.entity.FavoriteExchangeRatesEntity
import com.stefanenko.coinbase.data.util.coroutineDispatcher.BaseCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseService @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val dispatcher: BaseCoroutineDispatcher
) {

    suspend fun addExchangeRateToFavorite(
        favoriteExchangeRatesEntity: FavoriteExchangeRatesEntity
    ): Boolean {
        return withContext(dispatcher.io()) {
            val id = currencyDao.addCurrencyExchangeRateToFavorite(favoriteExchangeRatesEntity)
            id > 0
        }
    }

    suspend fun getExchangeRateList(): List<ExchangeRateEntity> {
        return withContext(dispatcher.io()) {
            currencyDao.getExchangeRates()
        }
    }

    suspend fun deleteExchangeRateFromFavorite(
        favoriteExchangeRatesEntity: FavoriteExchangeRatesEntity
    ): Boolean {
        return withContext(dispatcher.io()) {
            val amount = currencyDao.deleteExchangeRateFromFavorite(favoriteExchangeRatesEntity)
            amount > 0
        }
    }

    suspend fun getFavorites(): List<ExchangeRateEntity> {
        return withContext(dispatcher.io()) {
            currencyDao.getFavoritesData()
        }
    }

    /***
     * TODO add description of updating principle (clear old data and insert new)
     */
    suspend fun updateExchangeRateList(exchangeRateList: List<ExchangeRateEntity>): Boolean {
        return withContext(dispatcher.io()) {
            try {
                currencyDao.updateExchangeRateTable(exchangeRateList)
            } catch (e: Exception) {
                throw e
            }
        }
    }
}