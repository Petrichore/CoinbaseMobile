package com.stefanenko.coinbase.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule {
    @Provides
    fun provideAppContext(app: Application): Context = app
}