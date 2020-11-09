package com.stefanenko.coinbase.di

import android.app.Application
import com.stefanenko.coinbase.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ViewModelModule::class, FragmentModule::class])
interface AppComponent {

    fun injectApp(app: App)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(app: Application): Builder
    }
}