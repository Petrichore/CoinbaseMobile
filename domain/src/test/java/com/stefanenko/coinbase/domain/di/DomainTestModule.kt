package com.stefanenko.coinbase.domain.di

import com.stefanenko.coinbase.data.service.DatabaseService
import com.stefanenko.coinbase.data.service.OAuth2Service
import com.stefanenko.coinbase.data.service.RemoteDataService
import com.stefanenko.coinbase.data.service.webSocket.WebSocketService
import com.stefanenko.coinbase.domain.util.mapper.Mapper
import com.stefanenko.coinbase.domain.util.DateManager
import com.stefanenko.coinbase.domain.util.UrlBuilder
import com.stefanenko.coinbase.domain.util.oAuthScope.ScopeBuilder
import dagger.Module
import dagger.Provides
import io.mockk.mockk

@Module
class DomainTestModule {

    @Provides
    fun providesRemoteDataService(): RemoteDataService{
        return mockk()
    }

    @Provides
    fun providesDatabaseService(): DatabaseService{
        return mockk()
    }

    @Provides
    fun providesOAuth2Service(): OAuth2Service{
        return mockk()
    }

    @Provides
    fun providesMapper(): Mapper {
        return mockk()
    }

    @Provides
    fun providesScopeBuilder(): ScopeBuilder{
        return mockk()
    }

    @Provides
    fun providesWebSocketService(): WebSocketService {
        return mockk()
    }

    @Provides
    fun providesDateManager(): DateManager{
        return mockk()
    }

    @Provides
    fun providesUrlBuilder(): UrlBuilder{
        return mockk()
    }
}