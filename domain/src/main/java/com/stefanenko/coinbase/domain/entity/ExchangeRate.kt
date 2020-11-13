package com.stefanenko.coinbase.domain.entity

data class ExchangeRate(
    val currencyName: String,
    val exchangeRate: Double
)