package com.stefanenko.coinbase.data.service.webSocket

import okhttp3.WebSocket

sealed class RxWebSocketState {
    data class OnMessage(val data: String) : RxWebSocketState()
    data class OnOpen(val socket: WebSocket) : RxWebSocketState()
    data class OnError(val error: String): RxWebSocketState()
    object OnClose : RxWebSocketState()
}