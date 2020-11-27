package com.stefanenko.coinbase.domain.repository

import android.util.Log
import com.stefanenko.coinbase.data.service.webSocket.WebSocketService
import com.stefanenko.coinbase.domain.entity.CurrencyMarketInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealTimeDataRepository @Inject constructor(private val webSocketService: WebSocketService) {

    private val baseWebSocketUrl = "wss://www.bitmex.com/realtime?subscribe=trade:"

    internal fun subscribeOnCurrencyDataFlow(currency: String, onStateChanged: (List<CurrencyMarketInfo>) -> Unit) {
        val wsCurrencyDataUrl = "$baseWebSocketUrl$currency"

        webSocketService.startDataStream(wsCurrencyDataUrl) { response ->
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

    internal fun stopDataStream() {
        webSocketService.stopDataStream()
    }
}