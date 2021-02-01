package com.stefanenko.coinbase.data.di

import com.stefanenko.coinbase.data.database.dao.CurrencyDao
import com.stefanenko.coinbase.data.network.api.AuthApi
import com.stefanenko.coinbase.data.network.api.BitmexMarketApi
import com.stefanenko.coinbase.data.network.api.CoinbaseMarketApi
import com.stefanenko.coinbase.data.network.api.CoinbaseProfileApi
import com.stefanenko.coinbase.data.service.webSocket.RxWebSocketManager
import com.stefanenko.coinbase.data.util.NetworkResponseHandler
import com.stefanenko.coinbase.data.util.coroutineDispatcher.BaseCoroutineDispatcher
import com.stefanenko.coinbase.data.util.coroutineDispatcher.TestDispatchersProvider
import dagger.Module
import dagger.Provides
import io.mockk.mockk
import okhttp3.OkHttpClient

@Module
class DataTestModule {

    @Provides
    fun providesMockAuthApi(): AuthApi {
        return mockk()
    }

    @Provides
    fun providesMockCoinbaseMarketApi(): CoinbaseMarketApi {
        return mockk()
    }

    @Provides
    fun providesMockBitmexMarketApi(): BitmexMarketApi {
        return mockk()
    }

    @Provides
    fun providesMockCoinbaseProfileApi(): CoinbaseProfileApi {
        return mockk()
    }

    @Provides
    fun providesMockHttpClient(): OkHttpClient {
        return mockk()
    }

    @Provides
    fun provideResponseHandler(): NetworkResponseHandler{
        return mockk()
    }

    @Provides
    fun provideRxWebSocketManager(): RxWebSocketManager{
        return mockk()
    }

    @Provides
    fun provideCurrencyDao(): CurrencyDao{
        return mockk()
    }
}