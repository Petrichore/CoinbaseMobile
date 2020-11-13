package com.stefanenko.coinbase.domain.map

import com.stefanenko.coinbase.data.network.dto.token.RequestAccessToken
import com.stefanenko.coinbase.domain.entity.AccessToken

fun AccessToken.mapToRequestAccessToken(grantType: String): RequestAccessToken {
    return RequestAccessToken(clientId, clientSecret, grantType, redirectUri, authCode)
}