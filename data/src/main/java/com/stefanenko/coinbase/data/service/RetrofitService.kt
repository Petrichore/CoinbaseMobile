package com.stefanenko.coinbase.data.service

import com.stefanenko.coinbase.data.network.api.AuthApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitService @Inject constructor() {

    private val coinbaseAuthUrl = "https://api.coinbase.com"
    private val coinbaseUrl = "https://api.coinbase.com/v2/"
    private val bitmexUrl = "https://www.bitmex.com/api/v1/"

    private val okHttpBuilder = OkHttpClient.Builder()
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val retrofitAuth: Retrofit
    private val retrofitCoinbase: Retrofit
    private val retrofitBitmex: Retrofit
    private val client: OkHttpClient

    init {

        client = okHttpBuilder.addInterceptor(logger).build()

        retrofitAuth =
            Retrofit.Builder()
                .baseUrl(coinbaseAuthUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        retrofitCoinbase =
            Retrofit.Builder()
                .baseUrl(coinbaseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        retrofitBitmex =
            Retrofit.Builder()
                .baseUrl(bitmexUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    fun <T : AuthApi> createAuthService(authApiClass: Class<T>): T {
        return retrofitAuth.create(authApiClass)
    }

    fun <T> createCoinbaseService(coinbaseApiClass: Class<T>): T {
        return retrofitCoinbase.create(coinbaseApiClass)
    }

    fun <T> createBitmexService(bitmexApiClass: Class<T>): T{
        return retrofitBitmex.create(bitmexApiClass)
    }

    fun getHttpClient(): OkHttpClient{
        return client
    }
}