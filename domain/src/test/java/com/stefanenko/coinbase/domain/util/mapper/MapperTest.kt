package com.stefanenko.coinbase.domain.util.mapper

import com.google.common.truth.Truth.assertThat
import com.stefanenko.coinbase.data.database.entity.ExchangeRateEntity
import com.stefanenko.coinbase.data.database.entity.FavoriteExchangeRatesEntity
import com.stefanenko.coinbase.data.network.dto.activeCurrency.ActiveCurrencyResponse
import com.stefanenko.coinbase.data.network.dto.exchange.ResponseExchangerRates
import com.stefanenko.coinbase.data.network.dto.profile.ProfileCountry
import com.stefanenko.coinbase.data.network.dto.profile.ResponseProfile
import com.stefanenko.coinbase.domain.di.DaggerDomainComponentTest
import com.stefanenko.coinbase.domain.entity.ActiveCurrency
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.Profile
import com.stefanenko.coinbase.domain.util.DateManager
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import javax.inject.Inject

class MapperTest {

    @Inject
    lateinit var dateManagerTest: DateManager

    init {
        DaggerDomainComponentTest.builder().build().inject(this)
    }

    @Test
    fun `map ResponseExchangeRates to List of ExchangeRate`() {
        val date = "date"
        val time = "time"
        val baseCurrency = "USD"
        val currency1 = "BTC"
        val currency1Rate = "2048.0"

        val responseExchangeRates =
            ResponseExchangerRates(baseCurrency, mapOf(Pair(currency1, currency1Rate)))

        every { dateManagerTest.getCurrentDateAsString() } returns date
        every { dateManagerTest.getCurrentTimeAsString() } returns time

        val expectedList =
            listOf(ExchangeRate(baseCurrency, currency1, currency1Rate.toDouble(), date, time))
        val mapper = Mapper(dateManagerTest)
        val mapResult = mapper.map(responseExchangeRates)

        assertThat(mapResult).isEqualTo(expectedList)
    }

    @Test
    fun `map ResponseProfile to Profile`() {
        val userName = "userName"
        val email = "email"
        val imageUrl = "url"
        val countryCode = "code"
        val countryName = "countryName"
        val isInEurope = "false"
        val profileCountry = ProfileCountry(countryCode, countryName, isInEurope)
        val responseProfile = ResponseProfile(userName, email, imageUrl, profileCountry)
        val expectedProfile = Profile(userName, email, imageUrl, countryName)

        val mapper = Mapper(mockk())

        val profile = mapper.map(responseProfile)

        assertThat(profile).isEqualTo(expectedProfile)
    }

    @Test
    fun `map ExchangeRateEntity to ExchangeRate`() {
        val currencyName = "currencyName"
        val baseCurrencyName = "baseCurrencyName"
        val exchangeRateValue = 2048.0
        val date = "date"
        val time = "time"

        val exchangeRateEntity =
            ExchangeRateEntity(currencyName, baseCurrencyName, exchangeRateValue, date, time)
        val expectedExchangeRate =
            ExchangeRate(baseCurrencyName, currencyName, exchangeRateValue, date, time)

        val mapper = Mapper(mockk())

        val exchangeRateResponse = mapper.map(exchangeRateEntity)

        assertThat(exchangeRateResponse).isEqualTo(expectedExchangeRate)
    }

    @Test
    fun `map ActiveCurrencyResponse to ActiveCurrency`() {
        val currencyName = "currencyName"
        val rootSymbol = "rootSymbol"

        val activeCurrencyResponse = ActiveCurrencyResponse(currencyName, rootSymbol)
        val expectedActiveCurrency = ActiveCurrency(currencyName)

        val mapper = Mapper(mockk())

        val exchangeRateResponse = mapper.map(activeCurrencyResponse)

        assertThat(exchangeRateResponse).isEqualTo(expectedActiveCurrency)
    }

    @Test
    fun `map ExchangeRate to FavoriteExchangeRatesEntity`() {
        val currencyName = "currencyName"
        val baseCurrencyName = "baseCurrencyName"
        val exchangeRateValue = 2048.0
        val date = "date"
        val time = "time"

        val exchangeRate =
            ExchangeRate(baseCurrencyName, currencyName, exchangeRateValue, date, time)
        val expectedFavoriteExchangeRatesEntity = FavoriteExchangeRatesEntity(currencyName)

        val mapper = Mapper(mockk())

        val exchangeRateResponse = mapper.mapToFavoriteExchangeRateEntity(exchangeRate)

        assertThat(exchangeRateResponse).isEqualTo(expectedFavoriteExchangeRatesEntity)
    }

    @Test
    fun `map ExchangeRate to ExchangeRateEntity`() {
        val currencyName = "currencyName"
        val baseCurrencyName = "baseCurrencyName"
        val exchangeRateValue = 2048.0
        val date = "date"
        val time = "time"

        val exchangeRate =
            ExchangeRate(baseCurrencyName, currencyName, exchangeRateValue, date, time)
        val expectedExchangeRateEntity =
            ExchangeRateEntity(currencyName, baseCurrencyName, exchangeRateValue, date, time)

        val mapper = Mapper(mockk())

        val exchangeRateEntityResponse = mapper.mapToExchangeRateEntity(exchangeRate)

        assertThat(exchangeRateEntityResponse).isEqualTo(expectedExchangeRateEntity)
    }
}