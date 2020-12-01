package com.stefanenko.coinbase.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_exchange_rates")
data class FavoriteExchangeRatesEntity(
    @PrimaryKey
    @ColumnInfo(name = "name")
    val currencyName: String
)