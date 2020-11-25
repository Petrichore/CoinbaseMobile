package com.stefanenko.coinbase.domain.entity

data class ExchangeRate(
    val baseCurrency: String,
    val currencyName: String,
    val exchangeRate: Double,
    val date: String,
    val time: String
)