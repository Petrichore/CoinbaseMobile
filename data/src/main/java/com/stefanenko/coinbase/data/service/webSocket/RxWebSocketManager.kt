package com.stefanenko.coinbase.data.service.webSocket

import android.util.Log
import com.stefanenko.coinbase.data.service.RetrofitService
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import okhttp3.*
import okio.ByteString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RxWebSocketManager @Inject constructor(private val retrofitService: RetrofitService) {

    sealed class RxWebSocketState {
        data class OnMessage(val data: String) : RxWebSocketState()
        data class OnOpen(val socket: WebSocket) : RxWebSocketState()
        data class OnError(val error: String): RxWebSocketState()
        object OnClose : RxWebSocketState()
    }

    private val closeCode = 1000
    private val closeMessage = "Planned close"

    private lateinit var webSocket: WebSocket

    private lateinit var emitter: ObservableEmitter<RxWebSocketState>
    private lateinit var webSocketObservable: Observable<RxWebSocketState>

    fun start(url: String): Observable<RxWebSocketState> {
        webSocketObservable = Observable.create {
            emitter = it
        }
        performConnectToWebSocket(url)
        return webSocketObservable
    }

    fun stop() {
        webSocket.close(closeCode, closeMessage)
    }

    private fun performConnectToWebSocket(url: String) {
        val request = Request.Builder().url(url).build()
        createNewWebSocket(request)
    }

    private fun createNewWebSocket(request: Request){
        webSocket = retrofitService.getHttpClient().newWebSocket(request, getWebSocketListener())
    }

    private fun getWebSocketListener(): WebSocketListener {
        return object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                emitEvent(RxWebSocketState.OnOpen(webSocket))
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                Log.d("SOCKET STATE:::", "onMessageBytes")
                Log.d("SOCKET MESSAGE:::", bytes.toAsciiLowercase().utf8())
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                emitEvent(RxWebSocketState.OnMessage(text))
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d("SOCKET STATE:::", "onClosing")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                emitEvent(RxWebSocketState.OnClose)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.d("SOCKET STATE:::", "onFailure")

                emitEvent(RxWebSocketState.OnError(t.message ?: ""))
                t.printStackTrace()
            }
        }
    }

    private fun emitEvent(state: RxWebSocketState) {
        emitter.onNext(state)
    }
}