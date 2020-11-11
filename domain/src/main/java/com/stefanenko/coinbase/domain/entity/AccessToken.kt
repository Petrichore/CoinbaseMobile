package com.stefanenko.coinbase.domain.entity

data class AccessToken(
    val clientId: String,

    val clientSecret: String,

    val redirectUri: String,

    val authCode: String,

    val accessToken: String = "",

    val refreshToken: String = "",

    val tokenType: String = "",

    val scope: String = "",

    val expiresIn: Int = -1,

    val createdAt: Long = -1
)