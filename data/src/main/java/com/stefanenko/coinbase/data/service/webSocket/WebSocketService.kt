package com.stefanenko.coinbase.data.service.webSocket

import android.util.Log
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.stefanenko.coinbase.data.network.dto.socket.SocketResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketService @Inject constructor(private val rxWebSocketManager: RxWebSocketManager) {

    private val disposables = CompositeDisposable()

    fun startDataStream(url: String, onResponse: (SocketResponse) -> Unit) {
        disposables.add(
            rxWebSocketManager.start(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    it.printStackTrace()
                }
                .subscribe { socketState ->
                    handleWebSocketState(socketState, onResponse)
                })
    }

    fun stopDataStream() {
        rxWebSocketManager.stop()
    }

    private inline fun <reified T> handleWebSocketState(
        state: RxWebSocketManager.RxWebSocketState,
        onResponse: (T) -> Unit
    ) {
        when (state) {
            is RxWebSocketManager.RxWebSocketState.OnMessage -> {
                Log.d("Message", state.data)
                val responseTree = responseParser(state.data)
                if (responseTree.containsKey(ACTION) && responseTree[ACTION] != ACTION_PARTIAL) {
                    val socketResponse = Gson().fromJson(state.data, T::class.java)
                    onResponse.invoke(socketResponse)
                }
            }
            is RxWebSocketManager.RxWebSocketState.OnClose -> {
                Log.d("Socket closed", "CLOSED")
                disposables.clear()
            }
            is RxWebSocketManager.RxWebSocketState.OnOpen -> {
                Log.d("Socket open", "OPEN")
            }
            is RxWebSocketManager.RxWebSocketState.OnError -> {
                throw Exception(state.error)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun responseParser(response: String): LinkedTreeMap<String, *> {
        return Gson().fromJson(response, LinkedTreeMap::class.java) as LinkedTreeMap<String, *>
    }
}