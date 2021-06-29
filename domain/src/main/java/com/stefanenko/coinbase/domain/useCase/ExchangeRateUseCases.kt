package com.stefanenko.coinbase.domain.useCase

import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.repository.DataRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateUseCases @Inject constructor(private val dataRepository: DataRepository) {

    suspend fun getExchangeRates(baseCurrency: String): ResponseState<List<ExchangeRate>> {
        return dataRepository.getExchangeRates(baseCurrency)
    }

    suspend fun getCashedExchangeRates(): ResponseState<List<ExchangeRate>> {
        return dataRepository.getCashedExchangeRates()
    }

    suspend fun updateExchangeRates(baseCurrency: String): ResponseState<List<ExchangeRate>> {
        return dataRepository.updateExchangeRates(baseCurrency)
    }
}