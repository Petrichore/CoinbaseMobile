package com.stefanenko.coinbase.domain.entity

import com.google.gson.annotations.SerializedName

data class CurrencyMarketInfo(
    @SerializedName("symbol")
    val symbol: String,

    @SerializedName("side")
    val action: String,

    @SerializedName("price")
    val price: Double
)