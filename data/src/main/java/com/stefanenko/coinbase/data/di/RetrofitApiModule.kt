package com.stefanenko.coinbase.data.di

import com.stefanenko.coinbase.data.network.api.AuthApi
import com.stefanenko.coinbase.data.network.api.BitmexMarketApi
import com.stefanenko.coinbase.data.network.api.CoinbaseMarketApi
import com.stefanenko.coinbase.data.network.api.CoinbaseProfileApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
open class RetrofitApiModule {

    private val coinbaseAuthUrl = "https://api.coinbase.com"
    private val coinbaseUrl = "https://api.coinbase.com/v2/"
    private val bitmexUrl = "https://www.bitmex.com/api/v1/"

    protected val retrofitAuth: Retrofit
    protected val retrofitCoinbase: Retrofit
    protected val retrofitBitmex: Retrofit

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    init {
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

    @Provides
    open fun providesAuthApi(): AuthApi {
        return retrofitAuth.create(AuthApi::class.java)
    }

    @Provides
    open fun providesCoinbaseMarketApi(): CoinbaseMarketApi {
        return retrofitCoinbase.create(CoinbaseMarketApi::class.java)
    }

    @Provides
    open fun providesBitmexMarketApi(): BitmexMarketApi {
        return retrofitBitmex.create(BitmexMarketApi::class.java)
    }

    @Provides
    open fun providesCoinbaseProfileApi(): CoinbaseProfileApi {
        return retrofitCoinbase.create(CoinbaseProfileApi::class.java)
    }

    @Provides
    open fun providesHttpClient(): OkHttpClient {
        return client
    }
}