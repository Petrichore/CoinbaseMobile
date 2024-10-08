package com.stefanenko.coinbase.data.network.dto.token

import com.google.gson.annotations.SerializedName

data class RequestAccessToken(
    @SerializedName("client_id")
    val clientId: String,

    @SerializedName("client_secret")
    val clientSecret: String,

    @SerializedName("grant_type")
    val grantType: String,

    @SerializedName("redirect_uri")
    val redirectUri: String,

    @SerializedName("code")
    val authCode: String
)