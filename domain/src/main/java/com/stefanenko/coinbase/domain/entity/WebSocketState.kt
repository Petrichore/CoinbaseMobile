package com.stefanenko.coinbase.domain.entity

sealed class WebSocketState<T> {
    data class Data<T>(val data: T) : WebSocketState<T>()
    data class Error<T>(val error: String) : WebSocketState<T>()
    class Connected<T> : WebSocketState<T>()
    class Loading<T> : WebSocketState<T>()
    class ConnectionLost<T> : WebSocketState<T>()
}