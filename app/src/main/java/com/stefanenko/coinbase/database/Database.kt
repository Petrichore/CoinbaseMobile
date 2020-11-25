package com.stefanenko.coinbase.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stefanenko.coinbase.data.database.dao.CurrencyDao
import com.stefanenko.coinbase.data.database.entity.ExchangeRateEntity

@Database(entities = [ExchangeRateEntity::class], version = 2)
abstract class Database : RoomDatabase(){
    abstract fun getCurrencyDao(): CurrencyDao
}