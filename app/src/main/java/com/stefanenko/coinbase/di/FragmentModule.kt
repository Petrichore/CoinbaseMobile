package com.stefanenko.coinbase.di

import com.stefanenko.coinbase.ui.fragment.chart.ChartFragment
import com.stefanenko.coinbase.ui.fragment.exchangeRate.ExchangeRatesFragment
import com.stefanenko.coinbase.ui.fragment.profile.ProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun provideProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun provideExchangeRatesFragment(): ExchangeRatesFragment

    @ContributesAndroidInjector
    abstract fun provideChartFragment(): ChartFragment
}