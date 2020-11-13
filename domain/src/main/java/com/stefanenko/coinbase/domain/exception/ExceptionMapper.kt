package com.stefanenko.coinbase.domain.exception

import com.stefanenko.coinbase.data.exception.*

object ExceptionMapper {

    private const val UNEXPECTED_EXCEPTION = "Unexpected exception"

    fun mapToAppError(dataException: String): String {
        return when (dataException) {
            EXCEPTION_BAD_REQUEST -> ""
            EXCEPTION_UNAUTHORIZED -> ERROR_UNAUTHORIZED
            EXCEPTION_2FA_TOKEN -> ""
            EXCEPTION_INVALID_SCOPE -> ""
            EXCEPTION_NOT_FOUND -> ""
            EXCEPTION_TO_MANY_REQUESTS -> ""
            EXCEPTION_INTERNAL_SERVER_ERROR -> ""
            EXCEPTION_SERVICE_UNAVAILABLE -> ""
            EXCEPTION_NO_RESPONSE_DATA_EXCEPTION -> ""
            EXCEPTION_UNKNOWN_ERROR_CODE ->  ""
            else -> UNEXPECTED_EXCEPTION
        }
    }
}
