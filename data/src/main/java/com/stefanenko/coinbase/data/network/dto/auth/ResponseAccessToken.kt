package com.stefanenko.coinbase.data.network.dto.auth

import com.google.gson.annotations.SerializedName

data class ResponseAccessToken(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("scope")
    val scope: String,

    @SerializedName("expires_in")
    val expiresIn: Int,

    @SerializedName("created_at")
    val createdAt: Long
)