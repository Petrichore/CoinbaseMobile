package com.stefanenko.coinbase

import android.app.Application
import com.stefanenko.coinbase.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        DaggerAppComponent.builder().application(this).build().injectApp(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

}