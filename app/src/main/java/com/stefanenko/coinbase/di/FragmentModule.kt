package com.stefanenko.coinbase.di

import com.stefanenko.coinbase.ui.fragment.login.LoginFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector()
    abstract fun provideLoginFragment(): LoginFragment
}