package com.stefanenko.coinbase.data.di

import com.stefanenko.coinbase.data.BaseDataModuleTest
import com.stefanenko.coinbase.data.service.DatabaseServiceTest
import com.stefanenko.coinbase.data.service.OAuth2ServiceTest
import com.stefanenko.coinbase.data.service.RemoteDataServiceTest
import com.stefanenko.coinbase.data.service.webSocket.WebSocketManagerTest
import com.stefanenko.coinbase.data.service.webSocket.WebSocketServiceTest
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
@Component(
    modules = [
        DataTestModule::class
    ]
)
interface DataComponentTest {

    fun injectBase(baseDataModuleTest: BaseDataModuleTest)

    fun inject(remoteDataServiceTest: RemoteDataServiceTest)
    fun inject(databaseServiceTest: DatabaseServiceTest)
    fun inject(oAuth2Service: OAuth2ServiceTest)
    fun inject(webSocketServiceTest: WebSocketServiceTest)
    fun inject(webSocketManagerTest: WebSocketManagerTest)
}