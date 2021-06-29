package com.stefanenko.coinbase.di

import android.app.Application
import com.stefanenko.coinbase.App
import com.stefanenko.coinbase.data.di.CoroutineDispatcherModule
import com.stefanenko.coinbase.data.di.RetrofitApiModule
import com.stefanenko.coinbase.data.di.RxSchedulersProviderModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ViewModelModule::class,
        FragmentModule::class,
        ActivityModule::class,
        DatabaseModule::class,
        RetrofitApiModule::class,
        CoroutineDispatcherModule::class,
        RxSchedulersProviderModule::class
    ]
)
interface AppComponent {

    fun inject(app: App)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        fun databaseModule(databaseModule: DatabaseModule): Builder

        @BindsInstance
        fun application(app: Application): Builder
    }
}