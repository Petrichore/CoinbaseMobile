package com.stefanenko.coinbase.domain.useCase

import com.stefanenko.coinbase.domain.entity.CurrencyMarketInfo
import com.stefanenko.coinbase.domain.repository.RealTimeDataRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealTimeChartUseCases @Inject constructor(private val realTimeDataRepository: RealTimeDataRepository) {

    fun subscribeOnCurrencyDataFlow(currency: String, onStateChanged: (List<CurrencyMarketInfo>) -> Unit){
        realTimeDataRepository.subscribeOnCurrencyDataFlow(currency, onStateChanged)
    }

    fun unsubscribeFromCurrencyDataFlow(){
        realTimeDataRepository.stopDataStream()
    }
}