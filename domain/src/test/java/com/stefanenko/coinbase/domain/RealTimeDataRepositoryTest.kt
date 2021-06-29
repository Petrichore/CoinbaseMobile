package com.stefanenko.coinbase.domain

import com.BaseDomainModuleTest
import com.google.common.truth.Truth
import com.google.gson.Gson
import com.stefanenko.coinbase.data.network.dto.socket.CurrencyRateInRealTime
import com.stefanenko.coinbase.data.network.dto.socket.SocketResponse
import com.stefanenko.coinbase.data.service.webSocket.RxWebSocketManager
import com.stefanenko.coinbase.data.service.webSocket.WebSocketService
import com.stefanenko.coinbase.domain.di.DaggerDomainComponentTest
import com.stefanenko.coinbase.domain.entity.CurrencyMarketInfo
import com.stefanenko.coinbase.domain.entity.WebSocketState
import com.stefanenko.coinbase.domain.repository.RealTimeDataRepository
import com.stefanenko.coinbase.domain.util.mapper.Mapper
import io.mockk.*
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RealTimeDataRepositoryTest : BaseDomainModuleTest() {

    init {
        component.inject(this)
    }

    @Inject
    lateinit var webSocketService: WebSocketService

    @Inject
    lateinit var mapper: Mapper

    @Test
    fun `subscribeOnCurrencyRateDataFlow invokes callBack with proper arguments`() {
        val symbol = "BTC"
        val action = "trade"
        val price = 32640.0

        val currencyRateInRealTime = CurrencyRateInRealTime(symbol, action, price)
        val currencyRateInRealTimeList = listOf(currencyRateInRealTime)
        val socketResponse = SocketResponse("table", "action", currencyRateInRealTimeList)
        val expectedCurrencyMarketInfo = CurrencyMarketInfo(symbol, action, price.toFloat())
        val baseJson = Gson().toJson(socketResponse)

        val fakeDisposable: Disposable = mockk()

        val webSocketEvent = RxWebSocketManager.WebSocketEvent.OnMessage(baseJson)

        every { mapper.map(currencyRateInRealTime) } returns expectedCurrencyMarketInfo
        every { webSocketService.startDataStream(any(), any()) } answers {
            (this.invocation.args[1] as (RxWebSocketManager.WebSocketEvent) -> Unit).invoke(webSocketEvent)
            fakeDisposable
        }

        val realTimeDataRepository = RealTimeDataRepository(webSocketService, mapper)

        realTimeDataRepository.subscribeOnCurrencyRateDataFlow(symbol) {
            Truth.assertThat(it).isEqualTo(WebSocketState.Data(listOf(expectedCurrencyMarketInfo)))
        }
        verify(exactly = 1) { mapper.map(currencyRateInRealTime) }
    }
}
