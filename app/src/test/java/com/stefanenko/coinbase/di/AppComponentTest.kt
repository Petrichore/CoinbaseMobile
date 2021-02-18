package com.stefanenko.coinbase.di

import com.stefanenko.coinbase.BaseAppModuleTest
import com.stefanenko.coinbase.chart.ChartViewModelTest
import com.stefanenko.coinbase.chartFilter.ChartCurrencyFilterViewModelTest
import com.stefanenko.coinbase.exchangeRate.ExchangeRateViewModelTest
import com.stefanenko.coinbase.favorites.FavoritesViewModelTest
import com.stefanenko.coinbase.login.LoginViewModelTest
import com.stefanenko.coinbase.profile.ProfileViewModelTest
import com.stefanenko.coinbase.splash.SplashViewModelTest
import com.stefanenko.coinbase.util.viewModelErrorHandler.ViewModelErrorHandlerTest
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
@Component(
    modules = [
        AppModuleTest::class
    ]
)
interface AppComponentTest {

    fun inject(loginViewModelTest: LoginViewModelTest)
    fun inject(baseTest: BaseAppModuleTest)
    fun inject(splashViewModelTest: SplashViewModelTest)
    fun inject(exchangeRateViewModelTest: ExchangeRateViewModelTest)
    fun inject(favoritesViewModelTest: FavoritesViewModelTest)
    fun inject(favoritesChartViewModelTest: ChartViewModelTest)
    fun inject(filterViewModelTest: ChartCurrencyFilterViewModelTest)
    fun inject(profileViewModelTest: ProfileViewModelTest)
    fun inject(viewModelErrorHandlerTest: ViewModelErrorHandlerTest)
}