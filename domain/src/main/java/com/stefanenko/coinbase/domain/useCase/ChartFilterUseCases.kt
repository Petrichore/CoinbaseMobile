package com.stefanenko.coinbase.domain.useCase

import com.stefanenko.coinbase.domain.entity.ActiveCurrency
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.repository.DataRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChartFilterUseCases @Inject constructor(private val dataRepository: DataRepository){

    suspend fun getActiveCurrency(): ResponseState<List<ActiveCurrency>>{
        return dataRepository.getActiveCurrency()
    }
}