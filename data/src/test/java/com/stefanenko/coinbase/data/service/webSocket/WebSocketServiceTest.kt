package com.stefanenko.coinbase.data.service.webSocket

import com.google.common.truth.Truth
import com.google.gson.Gson
import com.stefanenko.coinbase.data.BaseDataModuleTest
import com.stefanenko.coinbase.data.network.dto.socket.CurrencyRateInRealTime
import com.stefanenko.coinbase.data.network.dto.socket.SocketResponse
import com.stefanenko.coinbase.data.util.scheduler.TestRxSchedulersProvider
import io.mockk.every
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class WebSocketServiceTest : BaseDataModuleTest() {

    init {
        component.inject(this)
    }

    @Inject
    lateinit var wsManager: RxWebSocketManager

    @Inject
    lateinit var schedulersProvider: TestRxSchedulersProvider

    @Test
    fun `dataStream emit data`() {
        val gson = Gson()

        val currencyRateRT1 = CurrencyRateInRealTime("BTC", "action", 64.0)
        val currencyRateRT2 = CurrencyRateInRealTime("BTC", "action", 128.0)
        val currencyRateRT3 = CurrencyRateInRealTime("BTC", "action", 256.0)
        val currencyRateRT4 = CurrencyRateInRealTime("BTC", "action", 512.0)

        val wsResponse1 = SocketResponse("table", "action", listOf(currencyRateRT1))
        val wsResponse2 = SocketResponse(
            "table",
            "action",
            listOf(currencyRateRT1, currencyRateRT2, currencyRateRT3)
        )
        val wsResponse3 = SocketResponse("table", "action", listOf(currencyRateRT4))

        val message1 = gson.toJson(wsResponse1)
        val message2 = gson.toJson(wsResponse2)
        val message3 = gson.toJson(wsResponse3)

        //set 3 event message
        val observable: Observable<RxWebSocketManager.WebSocketEvent> = Observable.just(
            RxWebSocketManager.WebSocketEvent.OnMessage(message1),
            RxWebSocketManager.WebSocketEvent.OnMessage(message2),
            RxWebSocketManager.WebSocketEvent.OnMessage(message3)
        )

        every { wsManager.start(any()) } returns observable

        val webSocketService = WebSocketService(wsManager, schedulersProvider)

        //count amount of event messages
        var counter = 0
        webSocketService.startDataStream("url") {
            counter++
        }
        Truth.assertThat(counter).isEqualTo(3)
    }
}