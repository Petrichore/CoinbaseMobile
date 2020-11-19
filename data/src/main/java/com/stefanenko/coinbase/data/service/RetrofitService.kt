package com.stefanenko.coinbase.data.service

import com.stefanenko.coinbase.data.network.api.AuthApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitService @Inject constructor() {

    private val baseUrlAuth = "https://api.coinbase.com"
    private val baseUrl = "https://api.coinbase.com/v2/"

    private val okHttpBuilder = OkHttpClient.Builder()
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val retrofitAuth: Retrofit
    private val retrofitBase: Retrofit
    private val client: OkHttpClient

    init {

        client = okHttpBuilder.addInterceptor(logger).build()

        retrofitAuth =
            Retrofit.Builder()
                .baseUrl(baseUrlAuth)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        retrofitBase =
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    fun <T : AuthApi> createAuthService(authServiceClass: Class<T>): T {
        return retrofitAuth.create(authServiceClass)
    }

    fun <T> createBaseService(baseServiceClass: Class<T>): T {
        return retrofitBase.create(baseServiceClass)
    }

    fun getHttpClient(): OkHttpClient{
        return client
    }
}