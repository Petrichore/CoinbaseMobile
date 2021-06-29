package com.stefanenko.coinbase.data.service.webSocket

import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import okhttp3.*
import okio.ByteString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RxWebSocketManager @Inject constructor(private val httpClient: OkHttpClient) {

    sealed class WebSocketEvent {
        data class OnMessage(val data: String) : WebSocketEvent()
        data class OnOpen(val socket: WebSocket) : WebSocketEvent()
        data class OnError(val error: String) : WebSocketEvent()
    }

    private val closeCode = 1000
    private val closeMessage = "Planned close"

    private lateinit var webSocket: WebSocket

    private lateinit var emitter: ObservableEmitter<WebSocketEvent>
    private lateinit var webSocketObservable: Observable<WebSocketEvent>

    fun start(url: String): Observable<WebSocketEvent> {
        webSocketObservable = Observable.create {
            emitter = it
        }
        performConnectToWebSocket(url)
        return webSocketObservable
    }

    private fun performConnectToWebSocket(url: String) {
        val request = Request.Builder().url(url).build()
        createNewWebSocket(request)
    }

    fun stop() {
        if (::webSocket.isInitialized) {
            webSocket.close(closeCode, closeMessage)
        }
    }

    private fun createNewWebSocket(request: Request) {
        if(::webSocket.isInitialized){
            webSocket.close(closeCode, closeMessage)
        }
        webSocket = httpClient.newWebSocket(request, getWebSocketListener())
    }

    private fun getWebSocketListener(): WebSocketListener {
        return object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                emitEvent(WebSocketEvent.OnOpen(webSocket))
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                Log.d("SOCKET STATE:::", "onMessageBytes")
                Log.d("SOCKET MESSAGE:::", bytes.toAsciiLowercase().utf8())
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                emitEvent(WebSocketEvent.OnMessage(text))
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d("SOCKET STATE:::", "onClosing")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.d("SOCKET STATE:::", "onFailure")

                emitEvent(WebSocketEvent.OnError(t.message ?: ""))
                t.printStackTrace()
            }
        }
    }

    private fun emitEvent(event: WebSocketEvent) {
        emitter.onNext(event)
    }
}