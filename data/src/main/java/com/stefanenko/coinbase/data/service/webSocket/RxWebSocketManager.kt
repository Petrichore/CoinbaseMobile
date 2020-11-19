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

    private val closeCode = 1000
    private val closeMessage = "Planned close"
    private val wsUrl = "wss://www.bitmex.com/realtime?subscribe=trade:XBTUSD"

    private lateinit var webSocket: WebSocket

    private lateinit var emitter: ObservableEmitter<RxWebSocketEvent>
    private lateinit var webSocketObservable: Observable<RxWebSocketEvent>

    fun start(): Observable<RxWebSocketEvent> {
        if (!::webSocket.isInitialized) {
            webSocketObservable = Observable.create {
                emitter = it
            }
            performConnectToWebSocket()
        } else {
            webSocket = retrofitService.getHttpClient()
                .newWebSocket(webSocket.request(), getWebSocketListener())
        }
        return webSocketObservable
    }

    fun stop() {
        webSocket.close(closeCode, closeMessage)
    }

    private fun performConnectToWebSocket() {
        val request = Request.Builder().url(wsUrl).build()
        webSocket = retrofitService.getHttpClient().newWebSocket(request, getWebSocketListener())
    }

    private fun getWebSocketListener(): WebSocketListener {
        return object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                emitEvent(RxWebSocketEvent.OnOpen(webSocket))
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                Log.d("SOCKET STATE:::", "onMessageBytes")
                Log.d("SOCKET MESSAGE:::", bytes.toAsciiLowercase().utf8())
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                emitEvent(RxWebSocketEvent.OnMessage(text))
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d("SOCKET STATE:::", "onClosing")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                emitEvent(RxWebSocketEvent.OnClose)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                t.printStackTrace()
                Log.d("SOCKET STATE:::", "onFailure")
            }
        }
    }

    private fun emitEvent(event: RxWebSocketEvent) {
        emitter.onNext(event)
    }
}