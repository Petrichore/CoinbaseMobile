package com.stefanenko.coinbase

import android.app.Application
import android.util.Log
import com.stefanenko.coinbase.di.DaggerAppComponent
import com.stefanenko.coinbase.di.DatabaseModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    companion object {
//        lateinit var authTokenManager: TokenManager
//            private set
    }

    override fun onCreate() {
        Log.d("APP", "App created")
        super.onCreate()
        initDagger()
        //authTokenManager = AuthTokenManager.build(clientId, clientSecret)
    }

    private fun initDagger() {
        val appComponent = DaggerAppComponent.builder()
            .application(this)
            .databaseModule(DatabaseModule(this))
            .build()

        appComponent.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}