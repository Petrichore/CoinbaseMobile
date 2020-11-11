package com.stefanenko.coinbase.data.exception

const val ERROR_BAD_REQUEST = "Bad Request Returns JSON with the error message"
const val ERROR_UNAUTHORIZED = "Unauthorized Couldn’t authenticate your request"
const val ERROR_2FA_TOKEN = "2FA Token required Re-try request with user’s 2FA token as CB-2FA-Token header"
const val ERROR_INVALID_SCOPE = "Invalid scope User hasn’t authorized necessary scope"
const val ERROR_NOT_FOUND = "Not Found No such object"
const val ERROR_TO_MANY_REQUESTS = "Too Many Requests Your connection is being rate limited"
const val ERROR_INTERNAL_SERVER_ERROR = "Internal Server Error Something went wrong"
const val ERROR_SERVICE_UNAVAILABLE = "Service Unavailable Your connection is being throttled or the service is down for"
const val ERROR_NO_RESPONSE_DATA_EXCEPTION = "NO_RESPONSE_DATA_EXCEPTION"
const val ERROR_UNKNOWN_ERROR_CODE = "ERROR_UNKNOWN_ERROR_CODE"








