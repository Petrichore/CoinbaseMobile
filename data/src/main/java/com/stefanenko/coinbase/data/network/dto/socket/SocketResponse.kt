package com.stefanenko.coinbase.data.network.dto.socket

import com.google.gson.annotations.SerializedName

data class SocketResponse(
    @SerializedName("table")
    val table: String,

    @SerializedName("action")
    val action: String,

    @SerializedName("data")
    val data: List<CurrencyRateInRealTime>
)