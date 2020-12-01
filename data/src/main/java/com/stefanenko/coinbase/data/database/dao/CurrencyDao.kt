package com.stefanenko.coinbase.data.database.dao

import androidx.room.*
import com.stefanenko.coinbase.data.database.entity.ExchangeRateEntity
import com.stefanenko.coinbase.data.database.entity.FavoriteExchangeRatesEntity

@Dao
interface CurrencyDao {

    @Query("select * from exchange_rate")
    suspend fun getExchangeRates(): List<ExchangeRateEntity>

    @Query("delete from exchange_rate")
    suspend fun clearExchangeRateTable()

    @Query("select * from fav_exchange_rates")
    suspend fun getFavorites(): List<FavoriteExchangeRatesEntity>

    @Query(
        "select exchange_rate.name, exchange_rate.base_currency_name, exchange_rate.exchange_rate,exchange_rate.add_date, exchange_rate.add_time from exchange_rate inner join fav_exchange_rates on fav_exchange_rates.name == exchange_rate.name"
    )
    suspend fun getFavoritesData(): List<ExchangeRateEntity>

    @Insert
    suspend fun insertCurrencyExchangeRates(exchangeRateList: List<ExchangeRateEntity>)

    @Update
    suspend fun updateExchangeRateTable(exchangeRateList: List<ExchangeRateEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCurrencyExchangeRateToFavorite(favoriteExchangeRatesEntity: FavoriteExchangeRatesEntity)

    @Delete
    suspend fun deleteExchangeRateFromFavorite(favoriteExchangeRatesEntity: FavoriteExchangeRatesEntity)
}