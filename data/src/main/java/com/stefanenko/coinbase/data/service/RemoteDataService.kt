package com.stefanenko.coinbase.data.service

import com.stefanenko.coinbase.data.exception.*
import com.stefanenko.coinbase.data.network.RetrofitService
import com.stefanenko.coinbase.data.network.api.AuthApi
import com.stefanenko.coinbase.data.network.dto.auth.RequestAccessToken
import com.stefanenko.coinbase.data.network.dto.auth.ResponseAccessToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataService @Inject constructor(private val retrofitService: RetrofitService) {

    private val authApi = retrofitService.createAuthService(AuthApi::class.java)

    suspend fun getAccessToken(requestAccessToken: RequestAccessToken): ResponseAccessToken {
        return withContext(Dispatchers.IO) {
            val response = authApi.getAccessToken(requestAccessToken)
            try {
                handleResponse(response)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private fun <T> handleResponse(response: Response<T>): T {
        if (response.isSuccessful) {
            return response.body() ?: throw Exception(ERROR_NO_RESPONSE_DATA_EXCEPTION)
        } else {
            when (response.code()) {
                400 -> throw Exception(ERROR_BAD_REQUEST)
                401 -> throw Exception(ERROR_UNAUTHORIZED)
                402 -> throw Exception(ERROR_2FA_TOKEN)
                403 -> throw Exception(ERROR_INVALID_SCOPE)
                404 -> throw Exception(ERROR_NOT_FOUND)
                429 -> throw Exception(ERROR_TO_MANY_REQUESTS)
                500 -> throw Exception(ERROR_INTERNAL_SERVER_ERROR)
                503 -> throw Exception(ERROR_SERVICE_UNAVAILABLE)
                else -> throw Exception(ERROR_UNKNOWN_ERROR_CODE)
            }
        }
    }
}
