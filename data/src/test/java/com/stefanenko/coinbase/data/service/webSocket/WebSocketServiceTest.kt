package com.stefanenko.coinbase.data.service.webSocket

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.stefanenko.coinbase.data.di.DaggerDataComponentTest
import com.stefanenko.coinbase.data.di.DataTestModule
import com.stefanenko.coinbase.data.network.dto.socket.CurrencyRateRT
import com.stefanenko.coinbase.data.network.dto.socket.SocketResponse
import com.stefanenko.coinbase.data.service.webSocket.RxWebSocketManager
import com.stefanenko.coinbase.data.service.webSocket.WebSocketService
import com.stefanenko.coinbase.data.util.scheduler.TestRxSchedulersProvider
import io.mockk.every
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import javax.inject.Inject

class WebSocketServiceTest {

    init {
        val component =
            DaggerDataComponentTest.builder().dataTestModule(DataTestModule()).build()
        component.inject(this)

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Inject
    lateinit var wsManager: RxWebSocketManager

    private val schedulersProvider = TestRxSchedulersProvider()

    @Test
    fun `dataStream emit data`() {
        val gson = Gson()

        val currencyRateRT1 = CurrencyRateRT("BTC", "action", 64.0)
        val currencyRateRT2 = CurrencyRateRT("BTC", "action", 128.0)
        val currencyRateRT3 = CurrencyRateRT("BTC", "action", 256.0)
        val currencyRateRT4 = CurrencyRateRT("BTC", "action", 512.0)

        val wsResponse1 = SocketResponse("table", "action", listOf(currencyRateRT1))
        val wsResponse2 = SocketResponse("table", "action", listOf(currencyRateRT1, currencyRateRT2, currencyRateRT3))
        val wsResponse3 = SocketResponse("table", "action", listOf(currencyRateRT4))

        val message1 = gson.toJson(wsResponse1)
        val message2 = gson.toJson(wsResponse2)
        val message3 = gson.toJson(wsResponse3)

        every { wsManager.start(any()) } returns Observable.just(
            RxWebSocketManager.RxWebSocketState.OnMessage(message1),
            RxWebSocketManager.RxWebSocketState.OnMessage(message2),
            RxWebSocketManager.RxWebSocketState.OnMessage(message3),
        )

        val webSocketService = WebSocketService(wsManager, schedulersProvider)

        var counter = 0
        webSocketService.startDataStream("url") {
            println(it.data)
            when (counter) {
                0 -> {
                    assertThat(it.data.size).isEqualTo(1)
                }
                1 -> {
                    assertThat(it.data.size).isEqualTo(3)
                }
                2 -> {
                    assertThat(it.data.size).isEqualTo(1)
                }
            }
            counter++
        }
    }
}