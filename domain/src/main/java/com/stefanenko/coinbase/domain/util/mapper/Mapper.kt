package com.stefanenko.coinbase.domain.util.mapper

import com.stefanenko.coinbase.data.database.entity.ExchangeRateEntity
import com.stefanenko.coinbase.data.database.entity.FavoriteExchangeRatesEntity
import com.stefanenko.coinbase.data.network.dto.activeCurrency.ActiveCurrencyResponse
import com.stefanenko.coinbase.data.network.dto.exchange.ResponseExchangerRates
import com.stefanenko.coinbase.data.network.dto.profile.ResponseProfile
import com.stefanenko.coinbase.domain.entity.ActiveCurrency
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.Profile
import com.stefanenko.coinbase.domain.util.DateManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Mapper @Inject constructor(private val dateManager: DateManager) {

    fun map(responseExchangerRates: ResponseExchangerRates): List<ExchangeRate> {
        val exchangeRates = mutableListOf<ExchangeRate>()
        responseExchangerRates.currencyRateMap.forEach { (key, value) ->
            exchangeRates.add(
                ExchangeRate(
                    responseExchangerRates.currency,
                    key,
                    value.toDouble(),
                    dateManager.getCurrentDateAsString(),
                    dateManager.getCurrentTimeAsString()
                )
            )
        }
        return exchangeRates
    }

    fun map(responseProfile: ResponseProfile): Profile = with(responseProfile) {
        Profile(name, email, imageUrl, country.name)
    }

    fun map(exchangeRateEntity: ExchangeRateEntity): ExchangeRate = with(exchangeRateEntity) {
        ExchangeRate(baseCurrencyName, currencyName, exchangeRate, addDate, addTime)
    }

    fun map(responseActiveCurrency: ActiveCurrencyResponse): ActiveCurrency =
        ActiveCurrency(responseActiveCurrency.name)

    fun mapToFavoriteExchangeRateEntity(exchangeRate: ExchangeRate): FavoriteExchangeRatesEntity =
        FavoriteExchangeRatesEntity(exchangeRate.currencyName)

    fun mapToExchangeRateEntity(exchangeRate: ExchangeRate): ExchangeRateEntity = with(exchangeRate){
        ExchangeRateEntity(currencyName, baseCurrency, this.exchangeRate, date, time)
    }
}