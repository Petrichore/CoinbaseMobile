package com.stefanenko.coinbase.data.di

import com.stefanenko.coinbase.data.util.scheduler.BaseRxSchedulersProvider
import com.stefanenko.coinbase.data.util.scheduler.RxSchedulersProvider
import dagger.Module
import dagger.Provides

@Module
class RxSchedulersProviderModule {

    @Provides
    fun providesRxSchedulersProvider(): BaseRxSchedulersProvider {
        return RxSchedulersProvider()
    }
}