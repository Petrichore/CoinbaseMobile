package com.stefanenko.coinbase.domain.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.stefanenko.coinbase.data.network.dto.socket.SocketResponse
import com.stefanenko.coinbase.data.service.webSocket.ACTION
import com.stefanenko.coinbase.data.service.webSocket.ACTION_PARTIAL
import com.stefanenko.coinbase.data.service.webSocket.RxWebSocketManager
import com.stefanenko.coinbase.data.service.webSocket.WebSocketService
import com.stefanenko.coinbase.domain.entity.CurrencyMarketInfo
import com.stefanenko.coinbase.domain.entity.WebSocketState
import com.stefanenko.coinbase.domain.util.mapper.Mapper
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealTimeDataRepository @Inject constructor(
    private val webSocketService: WebSocketService,
    private val mapper: Mapper
) {

    private val baseWebSocketUrl = "wss://www.bitmex.com/realtime?subscribe=trade:"

    internal fun subscribeOnCurrencyRateDataFlow(
        currency: String,
        onStateChanged: (WebSocketState<List<CurrencyMarketInfo>>) -> Unit
    ): Disposable {
        val wsCurrencyDataUrl = "$baseWebSocketUrl$currency"

        return webSocketService.startDataStream(wsCurrencyDataUrl) { event ->
            val webSocketState = handleWebSocketEvent(event)
            onStateChanged.invoke(webSocketState)
        }
    }

    internal fun stopDataStream(disposable: Disposable) {
        webSocketService.stopDataStream(disposable)
    }

    private fun handleWebSocketEvent(
        event: RxWebSocketManager.WebSocketEvent,
    ): WebSocketState<List<CurrencyMarketInfo>> {
        when (event) {
            is RxWebSocketManager.WebSocketEvent.OnMessage -> {
                Log.d("Message", event.data)
                val responseTree = responseParser(event.data)
                if (responseTree.containsKey(ACTION) && responseTree[ACTION] != ACTION_PARTIAL) {
                    val socketResponse = Gson().fromJson(event.data, SocketResponse::class.java)

                    val currencyInfoList = mutableListOf<CurrencyMarketInfo>()
                    currencyInfoList.addAll(socketResponse.data.map { mapper.map(it) })

                    return WebSocketState.Data(currencyInfoList)
                } else {
                    return WebSocketState.Loading()
                }
            }

            is RxWebSocketManager.WebSocketEvent.OnOpen -> {
                Log.d("Socket open", "OPEN")
                return WebSocketState.Connected()
            }

            is RxWebSocketManager.WebSocketEvent.OnError -> {
                //TODO change ERROR_MESSAGE to const
                return WebSocketState.Error("ERROR_MESSAGE")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun responseParser(response: String): LinkedTreeMap<String, *> {
        return Gson().fromJson(response, LinkedTreeMap::class.java) as LinkedTreeMap<String, *>
    }
}