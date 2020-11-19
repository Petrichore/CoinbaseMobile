package com.stefanenko.coinbase.data.service.webSocket

import android.util.Log
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.stefanenko.coinbase.data.network.dto.socket.CurrencyRateRT
import com.stefanenko.coinbase.data.network.dto.socket.SocketResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketService @Inject constructor(private val rxWebSocketManager: RxWebSocketManager) {

    private val disposables = CompositeDisposable()

    fun startDataStream(onResponse: (SocketResponse) -> Unit) {
        disposables.add(
            rxWebSocketManager.start()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ socketState ->
                    when (socketState) {
                        is RxWebSocketEvent.OnMessage -> {
                            Log.d("Message", socketState.data)
                            val responseTree = responseParser(socketState.data)
                            if (responseTree.containsKey("action")) {
                                if (responseTree["action"] != "partial") {
                                    val currencyRateRT = Gson().fromJson(
                                        socketState.data,
                                        SocketResponse::class.java
                                    )
                                    Log.d("Message", socketState.data)
                                    onResponse.invoke(currencyRateRT)
                                }
                            }
                        }
                        is RxWebSocketEvent.OnOpen -> {
                            Log.d("Socket open", "OPEN")
                        }
                        is RxWebSocketEvent.OnClose -> {
                            Log.d("Socket closed", "CLOSED")
                            disposables.clear()
                        }
                    }
                }, {}, {})
        )
    }

    fun stopDataStream() {
        rxWebSocketManager.stop()
    }

    private fun responseParser(response: String): LinkedTreeMap<String, *> {
        val gson = Gson()
        return gson.fromJson(response, LinkedTreeMap::class.java) as LinkedTreeMap<String, *>
    }
}