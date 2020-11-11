package com.stefanenko.coinbase.domain.map

import com.stefanenko.coinbase.data.network.dto.auth.RequestAccessToken
import com.stefanenko.coinbase.data.network.dto.auth.ResponseAccessToken
import com.stefanenko.coinbase.domain.entity.AccessToken

fun ResponseAccessToken.mapToAccessToken(requestToken: RequestAccessToken): AccessToken {
    return AccessToken(
        requestToken.clientId,
        requestToken.clientSecret,
        requestToken.redirectUri,
        requestToken.authCode,
        accessToken,
        refreshToken,
        tokenType,
        scope,
        expiresIn,
        createdAt
    )
}