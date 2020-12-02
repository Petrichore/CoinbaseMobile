package com.stefanenko.coinbase.domain.map

import com.stefanenko.coinbase.data.database.entity.ExchangeRateEntity
import com.stefanenko.coinbase.data.database.entity.FavoriteExchangeRatesEntity
import com.stefanenko.coinbase.domain.entity.ExchangeRate

fun ExchangeRateEntity.mapToExchangeRate(): ExchangeRate {
    return ExchangeRate(baseCurrencyName, currencyName, exchangeRate, addDate, addTime)
}

fun ExchangeRate.mapToExchangeRateEntity(): ExchangeRateEntity {
    return ExchangeRateEntity(currencyName, baseCurrency, exchangeRate, date, time)
}

fun ExchangeRate.mapToFavoriteExchangeRateEntity(): FavoriteExchangeRatesEntity {
    return FavoriteExchangeRatesEntity(currencyName)
}

