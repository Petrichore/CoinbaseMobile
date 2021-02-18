package com.stefanenko.coinbase.di

import com.stefanenko.coinbase.domain.useCase.*
import com.stefanenko.coinbase.util.Mapper
import com.stefanenko.coinbase.util.errorHandler.ViewModelErrorHandler
import com.stefanenko.coinbase.util.espressoIdleResource.EspressoIdlingResource
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import dagger.Module
import dagger.Provides
import io.mockk.mockk

@Module
class AppModuleTest {

    @Provides
    fun providesNetworkConnectivityManager(): NetworkConnectivityManager{
        return mockk()
    }

    @Provides
    fun providesAuthUseCases(): AuthUseCases{
        return mockk()
    }

    @Provides
    fun providesFavoriteUseCases(): FavoritesUseCases{
        return mockk()
    }

    @Provides
    fun providesAppModuleMapper(): Mapper {
        return mockk()
    }

    @Provides
    fun providesChartFilterUseCases(): ChartFilterUseCases {
        return mockk()
    }

    @Provides
    fun providesProfileUseCases(): ProfileUseCases {
        return mockk()
    }

    @Provides
    fun providesViewModelErrorHandler(): ViewModelErrorHandler {
        return mockk()
    }

    @Provides
    fun providesChartUseCases(): ChartUseCases{
        return mockk()
    }

    @Provides
    fun providesExchangeRateUseCases(): ExchangeRateUseCases {
        return mockk()
    }

    @Provides
    fun providesAuthPref(): AuthPreferences{
        return mockk()
    }
}