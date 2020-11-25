package com.stefanenko.coinbase.data.service

import com.stefanenko.coinbase.data.database.dao.CurrencyDao
import com.stefanenko.coinbase.data.database.entity.ExchangeRateEntity
import com.stefanenko.coinbase.data.exception.EXCEPTION_CURRENCY_SAVE_UNEQUALS_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseService @Inject constructor(private val currencyDao: CurrencyDao) {

    suspend fun addCurrencyToFavorite(exchangeRate: ExchangeRateEntity): Boolean {
        return withContext(Dispatchers.IO) {
            currencyDao.insertCurrencyRate(exchangeRate)
            true
        }
    }

    suspend fun getFavorites(): List<ExchangeRateEntity> {
        return withContext(Dispatchers.IO) {
            currencyDao.getCurrencyRate() ?: throw Exception(EXCEPTION_CURRENCY_SAVE_UNEQUALS_ID)
        }
    }
}