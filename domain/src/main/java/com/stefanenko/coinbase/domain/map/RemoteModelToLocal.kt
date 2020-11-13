package com.stefanenko.coinbase.domain.map

import com.stefanenko.coinbase.data.network.dto.token.RequestAccessToken
import com.stefanenko.coinbase.data.network.dto.token.ResponseAccessToken
import com.stefanenko.coinbase.data.network.dto.exchange.ResponseExchangerRates
import com.stefanenko.coinbase.data.network.dto.profile.ResponseProfile
import com.stefanenko.coinbase.domain.entity.AccessToken
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.Profile

fun ResponseAccessToken.mapToAccessToken(requestToken: RequestAccessToken): AccessToken {
    return AccessToken(
        requestToken.clientId,
        requestToken.clientSecret,
        requestToken.redirectUri,
        requestToken.authCode,
        accessToken,
        refreshToken,
        tokenType,
        scope,
        expiresIn,
        createdAt
    )
}

fun ResponseExchangerRates.mapToExchangeRates(): List<ExchangeRate> {
    val exchangeRates = mutableListOf<ExchangeRate>()
    ratesList.forEach { key, value ->
        exchangeRates.add(ExchangeRate(key, value.toDouble()))
    }
    return exchangeRates
}

fun ResponseProfile.mapToProfile(): Profile {
    return Profile(name, email, imageUrl, country.name)
}