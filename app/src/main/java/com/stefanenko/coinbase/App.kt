package com.stefanenko.coinbase

import android.app.Application
import com.coinbase.Coinbase
import com.coinbase.CoinbaseBuilder
import com.stefanenko.coinbase.di.AppComponent
import com.stefanenko.coinbase.di.DaggerAppComponent
import com.stefanenko.coinbase.util.preferences.ClientPreferences
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var clientPreferences: ClientPreferences

    private val clientId = "979fbe1bf76735a1179d82729ed941ffdd5331686891c10c12d1b25f24ac5293"
    private val clientSecret = "208dfff66f3af0ad840541b2dc41f417ce785f187913acb6837bdb39e338e207"

    companion object {
        lateinit var coinbase: Coinbase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()
        clientPreferences.saveClientId()
        clientPreferences.saveClientSecret()
    }

    private fun configCoinbase(): Coinbase {
        return CoinbaseBuilder.withClientIdAndSecret(this, clientId, clientSecret).build()
    }

    private fun initDagger() {
        DaggerAppComponent.builder().application(this).build().injectApp(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

}