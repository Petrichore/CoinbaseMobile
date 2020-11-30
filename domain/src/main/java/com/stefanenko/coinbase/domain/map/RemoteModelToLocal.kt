package com.stefanenko.coinbase.domain.map

import com.stefanenko.coinbase.data.network.dto.activeCurrency.ActiveCurrencyResponse
import com.stefanenko.coinbase.data.network.dto.exchange.ResponseExchangerRates
import com.stefanenko.coinbase.data.network.dto.profile.ResponseProfile
import com.stefanenko.coinbase.domain.entity.ActiveCurrency
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.Profile
import com.stefanenko.coinbase.domain.util.DateManager

fun ResponseExchangerRates.mapToExchangeRates(baseCurrency: String): List<ExchangeRate> {
    val exchangeRates = mutableListOf<ExchangeRate>()
    ratesList.forEach { (key, value) ->
        exchangeRates.add(
            ExchangeRate(
                baseCurrency,
                key,
                value.toDouble(),
                DateManager.getCurrentDateAsString(),
                DateManager.getCurrentTimeAsString()
            )
        )
    }
    return exchangeRates
}

fun ResponseProfile.mapToProfile(): Profile {
    return Profile(name, email, imageUrl, country.name)
}