package com.stefanenko.coinbase.data.di

import com.stefanenko.coinbase.data.service.DatabaseServiceTest
import com.stefanenko.coinbase.data.service.OAuth2ServiceTest
import com.stefanenko.coinbase.data.service.RemoteDataServiceTest
import com.stefanenko.coinbase.data.service.webSocket.WebSocketServiceTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DataTestModule::class
    ]
)
interface DataComponentTest {

    fun inject(remoteDataServiceTest: RemoteDataServiceTest)
    fun inject(databaseServiceTest: DatabaseServiceTest)
    fun inject(oAuth2Service: OAuth2ServiceTest)
    fun inject(webSocketServiceTest: WebSocketServiceTest)

    @Component.Builder
    interface Builder {
        fun build(): DataComponentTest

        fun dataTestModule(databaseModule: DataTestModule): Builder
    }
}