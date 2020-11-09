package com.stefanenko.coinbase.domain.entity

data class CurrencyExchangeRate(
    val currencyName: String,
    val exchangeRate: Double
)