package com.stefanenko.coinbase.data.util

import com.stefanenko.coinbase.data.exception.*
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkResponseHandler @Inject constructor() {
    fun <T> handleResponse(response: Response<T>): T {
        if (response.isSuccessful) {
            return response.body() ?: throw Exception(EXCEPTION_NO_RESPONSE_DATA_EXCEPTION)
        } else {
            when (response.code()) {
                400 -> throw Exception(EXCEPTION_BAD_REQUEST)
                401 -> throw Exception(EXCEPTION_UNAUTHORIZED)
                402 -> throw Exception(EXCEPTION_2FA_TOKEN)
                403 -> throw Exception(EXCEPTION_INVALID_SCOPE)
                404 -> throw Exception(EXCEPTION_NOT_FOUND)
                429 -> throw Exception(EXCEPTION_TO_MANY_REQUESTS)
                500 -> throw Exception(EXCEPTION_INTERNAL_SERVER_ERROR)
                503 -> throw Exception(EXCEPTION_SERVICE_UNAVAILABLE)
                else -> throw Exception(EXCEPTION_UNKNOWN_ERROR_CODE)
            }
        }
    }
}