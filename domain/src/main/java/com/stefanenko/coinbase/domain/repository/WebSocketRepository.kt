package com.stefanenko.coinbase.domain.repository

import android.util.Log
import com.stefanenko.coinbase.data.service.webSocket.WebSocketService
import com.stefanenko.coinbase.domain.entity.CurrencyMarketInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketRepository @Inject constructor(private val webSocketService: WebSocketService) {

    fun subscribeOnCurrencyData(onStateChanged: (List<CurrencyMarketInfo>) -> Unit) {
        webSocketService.startDataStream { response ->
            val currencyInfoList = mutableListOf<CurrencyMarketInfo>()
            currencyInfoList.addAll(response.data.map {
                CurrencyMarketInfo(
                    it.symbol,
                    it.action,
                    it.price.toFloat()
                )
            })
            Log.d("Response: ", "$currencyInfoList")
            onStateChanged.invoke(currencyInfoList)
        }
    }

    fun stopDataStream() {
        webSocketService.stopDataStream()
    }
}