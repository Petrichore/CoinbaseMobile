package com.stefanenko.coinbase.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.stefanenko.coinbase.data.database.entity.ExchangeRateEntity

@Dao
interface CurrencyDao {

    @Query("select * from exchange_rate")
    suspend fun getCurrencyRate(): List<ExchangeRateEntity>?

    @Insert
    suspend fun insertCurrencyRate(exchangeRate: ExchangeRateEntity): Long

    @Delete
    suspend fun deleteExchangeRate(exchangeRate: ExchangeRateEntity): Int
}