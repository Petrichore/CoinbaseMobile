package com.stefanenko.coinbase.domain.map

import com.stefanenko.coinbase.data.database.entity.ExchangeRateEntity
import com.stefanenko.coinbase.domain.entity.ExchangeRate

fun ExchangeRate.mapToExchangeRateEntity(): ExchangeRateEntity {
    return ExchangeRateEntity(baseCurrency, currencyName, exchangeRate, date, time)
}