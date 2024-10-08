package com.stefanenko.coinbase.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stefanenko.coinbase.data.database.dao.CurrencyDao
import com.stefanenko.coinbase.data.database.entity.ExchangeRateEntity
import com.stefanenko.coinbase.data.database.entity.FavoriteExchangeRatesEntity

@Database(entities = [ExchangeRateEntity::class, FavoriteExchangeRatesEntity::class], version = 3)
abstract class Database : RoomDatabase() {
    abstract fun getCurrencyDao(): CurrencyDao
}