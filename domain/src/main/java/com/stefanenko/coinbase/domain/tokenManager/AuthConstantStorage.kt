package com.stefanenko.coinbase.domain.tokenManager

const val BASE_AUTH_URL = "https://www.coinbase.com/oauth/authorize"
const val REDIRECT_URI_VALUE = "urn:ietf:wg:oauth:2.0:oob"

internal const val RESPONSE_TYPE_KEY = "response_type"
internal const val RESPONSE_TYPE_VALUE = "code"

internal const val REDIRECT_URI_KEY = "redirect_uri"

internal const val CLIENT_ID_KEY = "client_id"
internal const val SCOPE_KEY = "scope"

const val GRANT_TYPE_ACCESS_TOKEN = "authorization_code"
const val GRANT_TYPE_REFRESH_TOKEN = "refresh_token"