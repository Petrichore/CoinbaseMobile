package com.stefanenko.coinbase.di

import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.activity.splash.SplashActivity
import com.stefanenko.coinbase.ui.activity.login.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun provideSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun provideLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun provideMainActivity(): MainActivity
}