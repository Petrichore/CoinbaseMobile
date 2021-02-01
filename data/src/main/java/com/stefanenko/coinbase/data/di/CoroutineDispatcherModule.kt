package com.stefanenko.coinbase.data.di

import com.stefanenko.coinbase.data.util.coroutineDispatcher.BaseCoroutineDispatcher
import com.stefanenko.coinbase.data.util.coroutineDispatcher.DispatchersProvider
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class CoroutineDispatcherModule {

    @Provides
    fun providesDispatcher(): BaseCoroutineDispatcher{
        return DispatchersProvider()
    }
}