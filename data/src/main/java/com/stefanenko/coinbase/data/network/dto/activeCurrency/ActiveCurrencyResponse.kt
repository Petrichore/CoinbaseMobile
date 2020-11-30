package com.stefanenko.coinbase.data.network.dto.activeCurrency

import com.google.gson.annotations.SerializedName

data class ActiveCurrencyResponse(
    @SerializedName("symbol")
    val name: String,

    @SerializedName("rootSymbol")
    val rootSymbol: String
)