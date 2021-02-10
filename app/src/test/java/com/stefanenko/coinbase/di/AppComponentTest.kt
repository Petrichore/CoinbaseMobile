package com.stefanenko.coinbase.di

import com.stefanenko.coinbase.BaseAppModuleTest
import com.stefanenko.coinbase.chart.ChartViewModelTest
import com.stefanenko.coinbase.chartFilter.ChartFilterViewModelTest
import com.stefanenko.coinbase.exchangeRate.ExchangeRateViewModelTest
import com.stefanenko.coinbase.favorites.FavoritesViewModelTest
import com.stefanenko.coinbase.login.LoginViewModelTest
import com.stefanenko.coinbase.splash.SplashViewModelTest
import com.stefanenko.coinbase.ui.fragment.chart.chartFilter.FilterViewModel
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
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
    fun inject(filterViewModelTest: ChartFilterViewModelTest)
}