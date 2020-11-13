package com.stefanenko.coinbase.data.network.dto


data class DefaultMarketRequest<T>(
    val accessToken: String,
    val refreshToken: String,
    val params: T
)