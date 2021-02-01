package com.stefanenko.coinbase.domain.di

import com.stefanenko.coinbase.domain.AuthManagerTest
import com.stefanenko.coinbase.domain.DataRepositoryTest
import com.stefanenko.coinbase.domain.util.mapper.MapperTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DomainTestModule::class
    ]
)
interface DomainComponentTest {

    fun inject(dataRepositoryTest: DataRepositoryTest)
    fun inject(authManagerTest: AuthManagerTest)
    fun inject(mapperTest: MapperTest)
}