package com.stefanenko.coinbase.data.service.webSocket

import okhttp3.WebSocket

sealed class RxWebSocketEvent {
    data class OnMessage(val data: String) : RxWebSocketEvent()
    data class OnOpen(val socket: WebSocket) : RxWebSocketEvent()
    object OnClose : RxWebSocketEvent()
}