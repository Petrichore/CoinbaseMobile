package com.stefanenko.coinbase.data.network.dto.token

import com.google.gson.annotations.SerializedName

data class RequestRevokeToken(
    @SerializedName("token")
    val bearerToken: String
)