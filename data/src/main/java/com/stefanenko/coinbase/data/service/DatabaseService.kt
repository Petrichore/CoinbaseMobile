package com.stefanenko.coinbase.data.service

import android.util.Log
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

    suspend fun addExchangeRate(exchangeRate: ExchangeRateEntity): Long {
        return withContext(Dispatchers.IO) {
            currencyDao.insertCurrencyRate(exchangeRate)
        }
    }

    suspend fun deleteExchangeRate(exchangeRate: ExchangeRateEntity): Boolean {
        return withContext(Dispatchers.IO) {
            Log.d("Delete Item", "YEEEEES")
            currencyDao.deleteExchangeRate(exchangeRate)
            true
        }
    }

    suspend fun getFavorites(): List<ExchangeRateEntity> {
        return withContext(Dispatchers.IO) {
            currencyDao.getCurrencyRate() ?: throw Exception(EXCEPTION_CURRENCY_SAVE_UNEQUALS_ID)
        }
    }
}