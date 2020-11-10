package com.stefanenko.coinbase.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stefanenko.coinbase.ui.activity.login.LoginViewModel
import com.stefanenko.coinbase.ui.base.ViewModelFactory
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
}