package com.stefanenko.coinbase.data.network.dto.socket

import com.google.gson.annotations.SerializedName

data class CurrencyRateInRealTime(
    @SerializedName("symbol")
    val symbol: String,

    @SerializedName("side")
    val action: String,

    @SerializedName("price")
    val price: Double
)