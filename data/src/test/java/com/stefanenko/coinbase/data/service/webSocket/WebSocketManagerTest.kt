package com.stefanenko.coinbase.data.service.webSocket

import com.google.common.truth.Truth.assertThat
import com.stefanenko.coinbase.data.BaseDataModuleTest
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.WebSocket
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class WebSocketManagerTest : BaseDataModuleTest() {

    init {
        component.inject(this)
    }

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Test
    fun `start method return observable`() {
        val url = "wss://www.bitmex.com/realtime?subscribe=trade:"
        val webSocket: WebSocket = mockk()
        every { okHttpClient.newWebSocket(any(), any()) } returns webSocket

        val webSocketManager = RxWebSocketManager(okHttpClient)
        val observable = webSocketManager.start(url)

        assertThat(observable).isInstanceOf(Observable::class.java)
    }
}