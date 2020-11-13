package com.stefanenko.coinbase.data.network.dto.exchange

import com.google.gson.annotations.SerializedName

data class ResponseExchangerRates(

    @SerializedName("currency")
    val currency: String,

    @SerializedName("rates")
    val ratesList: Map<String, String>
)