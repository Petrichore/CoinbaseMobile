package com.stefanenko.coinbase.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stefanenko.coinbase.ui.activity.login.LoginViewModel
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import com.stefanenko.coinbase.ui.fragment.chart.ChartViewModel
import com.stefanenko.coinbase.ui.fragment.exchangeRate.ExchangeRatesViewModel
import com.stefanenko.coinbase.ui.fragment.profile.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(loginViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExchangeRatesViewModel::class)
    abstract fun bindExchangeRateViewModel(exchangeRatesViewModel: ExchangeRatesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChartViewModel::class)
    abstract fun bindChartViewModel(chartViewModel: ChartViewModel): ViewModel
}