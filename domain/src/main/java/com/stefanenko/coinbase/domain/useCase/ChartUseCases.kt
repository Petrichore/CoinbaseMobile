package com.stefanenko.coinbase.domain.useCase

import com.stefanenko.coinbase.domain.entity.CurrencyMarketInfo
import com.stefanenko.coinbase.domain.entity.WebSocketState
import com.stefanenko.coinbase.domain.repository.RealTimeDataRepository
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChartUseCases @Inject constructor(private val realTimeDataRepository: RealTimeDataRepository) {

    fun subscribeOnCurrencyDataFlow(
        currency: String,
        onStateChanged: (WebSocketState<List<CurrencyMarketInfo>>) -> Unit
    ): Disposable {
        return realTimeDataRepository.subscribeOnCurrencyRateDataFlow(currency, onStateChanged)
    }

    fun unsubscribeFromCurrencyDataFlow(disposable: Disposable) {
        realTimeDataRepository.stopDataStream(disposable)
    }
}