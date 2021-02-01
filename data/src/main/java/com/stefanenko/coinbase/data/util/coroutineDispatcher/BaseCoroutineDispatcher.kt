package com.stefanenko.coinbase.data.util.coroutineDispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface BaseCoroutineDispatcher {
    fun io(): CoroutineDispatcher

    fun default(): CoroutineDispatcher

    fun main(): CoroutineDispatcher
}