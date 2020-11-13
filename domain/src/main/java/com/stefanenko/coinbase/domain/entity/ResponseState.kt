package com.stefanenko.coinbase.domain.entity

sealed class ResponseState<T> {
    data class Data<T>(val data: T) : ResponseState<T>()
    data class Error<T>(val error: String) : ResponseState<T>()
    class InProgress<T> : ResponseState<T>()
}