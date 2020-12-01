package com.stefanenko.coinbase.di

import com.stefanenko.coinbase.ui.fragment.chart.ChartFragment
import com.stefanenko.coinbase.ui.fragment.chart.chartFilter.FilterFragment
import com.stefanenko.coinbase.ui.fragment.exchangeRate.ExchangeRatesFragment
import com.stefanenko.coinbase.ui.fragment.favorites.FavoritesFragment
import com.stefanenko.coinbase.ui.fragment.profile.ProfileFragment
import com.stefanenko.coinbase.ui.fragment.profile.settings.SettingsFragment
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

    @ContributesAndroidInjector
    abstract fun provideFavoritesFragment(): FavoritesFragment

    @ContributesAndroidInjector
    abstract fun provideFilterFragment(): FilterFragment

    @ContributesAndroidInjector
    abstract fun provideSettingsFragment(): SettingsFragment
}