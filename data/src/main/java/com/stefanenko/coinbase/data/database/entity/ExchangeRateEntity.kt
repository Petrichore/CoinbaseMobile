package com.stefanenko.coinbase.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rate")
data class ExchangeRateEntity(

    @PrimaryKey
    @ColumnInfo(name = "name")
    val currencyName: String,

    @ColumnInfo(name = "base_currency_name")
    val baseCurrencyName: String,

    @ColumnInfo(name = "exchange_rate")
    val exchangeRate: Double,

    @ColumnInfo(name = "add_date")
    val addDate: String,

    @ColumnInfo(name = "add_time")
    val addTime: String
)