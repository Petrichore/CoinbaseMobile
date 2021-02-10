package com.stefanenko.coinbase.chart

import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.Entry
import com.stefanenko.coinbase.BaseAppModuleTest
import com.stefanenko.coinbase.domain.entity.CurrencyMarketInfo
import com.stefanenko.coinbase.domain.entity.WebSocketState
import com.stefanenko.coinbase.domain.useCase.ChartUseCases
import com.stefanenko.coinbase.ui.fragment.chart.ChartViewModel
import com.stefanenko.coinbase.ui.fragment.chart.StateChart
import com.stefanenko.coinbase.ui.fragment.exchangeRate.ExchangeRatesViewModel
import com.stefanenko.coinbase.ui.fragment.exchangeRate.StateExchangeRates
import com.stefanenko.coinbase.util.Mapper
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import io.mockk.*
import io.mockk.verifyOrder
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ChartViewModelTest : BaseAppModuleTest() {

    init {
        component.inject(this)
    }

    @Inject
    lateinit var networkConnectivityManager: NetworkConnectivityManager

    @Inject
    lateinit var chartUseCases: ChartUseCases

    @Inject
    lateinit var mapper: Mapper

    private val stateObserver: Observer<StateChart> = mockk()

    @Before
    fun setUp() {
        every { stateObserver.onChanged(any()) } returns Unit
    }

    @Test
    fun `viewModel state when connect to webSocket successfully`() {
        val currency = "XBTUSD"
        val disposable: Disposable = mockk()

        val webSocketState: WebSocketState.Connected<List<CurrencyMarketInfo>> =
            WebSocketState.Connected()

        every { networkConnectivityManager.isConnected() } returns true
        coEvery { chartUseCases.subscribeOnCurrencyDataFlow(currency, any()) } answers {
            (this.invocation.args[1] as (WebSocketState<List<CurrencyMarketInfo>>) -> Unit).invoke(
                webSocketState
            )
            disposable
        }

        val viewModel = ChartViewModel(chartUseCases, networkConnectivityManager, mapper)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.subscribeOnCurrencyDataFlow(currency)
            verifyOrder {
                stateObserver.onChanged(StateChart.StartLoading)
                stateObserver.onChanged(StateChart.OnConnectToWebSocket)
                stateObserver.onChanged(StateChart.StopLoading)
            }
        }
    }

    @Test
    fun `viewModel state when webSocket returns new message`() {
        val symbol = "symbol"
        val action = "action"
        val price = 64.0f

        val currency = "XBTUSD"
        val disposable: Disposable = mockk()
        val currencyMarketInfoList = listOf(
            CurrencyMarketInfo(symbol, action, price),
            CurrencyMarketInfo(symbol, action, price),
            CurrencyMarketInfo(symbol, action, price)
        )
        val fakeList: List<Entry> = mockk()

        val webSocketStateConnected: WebSocketState.Connected<List<CurrencyMarketInfo>> =
            WebSocketState.Connected()
        val webSocketStateData: WebSocketState.Data<List<CurrencyMarketInfo>> =
            WebSocketState.Data(currencyMarketInfoList)

        every { networkConnectivityManager.isConnected() } returns true
        every { mapper.map(currencyMarketInfoList, any()) } returns fakeList
        coEvery { chartUseCases.subscribeOnCurrencyDataFlow(currency, any()) } answers {
            (this.invocation.args[1] as (WebSocketState<List<CurrencyMarketInfo>>) -> Unit).invoke(
                webSocketStateConnected
            )
            (this.invocation.args[1] as (WebSocketState<List<CurrencyMarketInfo>>) -> Unit).invoke(
                webSocketStateData
            )
            disposable
        }

        val viewModel = ChartViewModel(chartUseCases, networkConnectivityManager, mapper)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.subscribeOnCurrencyDataFlow(currency)
            verifyOrder {
                stateObserver.onChanged(StateChart.StartLoading)
                stateObserver.onChanged(StateChart.OnConnectToWebSocket)
                stateObserver.onChanged(StateChart.StopLoading)
                stateObserver.onChanged(StateChart.OnNewMessage(fakeList))
            }
        }
    }

    @Test
    fun `viewModel state when webSocket returns error`() {
        val currency = "XBTUSD"
        val errorMessage = "webSocket test error"
        val disposable: Disposable = mockk()

        val webSocketStateConnected: WebSocketState.Connected<List<CurrencyMarketInfo>> =
            WebSocketState.Connected()
        val webSocketStateError: WebSocketState.Error<List<CurrencyMarketInfo>> =
            WebSocketState.Error(errorMessage)

        every { networkConnectivityManager.isConnected() } returns true
        coEvery { chartUseCases.subscribeOnCurrencyDataFlow(currency, any()) } answers {
            (this.invocation.args[1] as (WebSocketState<List<CurrencyMarketInfo>>) -> Unit).invoke(
                webSocketStateConnected
            )
            (this.invocation.args[1] as (WebSocketState<List<CurrencyMarketInfo>>) -> Unit).invoke(
                webSocketStateError
            )
            disposable
        }

        val viewModel = ChartViewModel(chartUseCases, networkConnectivityManager, mapper)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.subscribeOnCurrencyDataFlow(currency)
            verifyOrder {
                stateObserver.onChanged(StateChart.StartLoading)
                stateObserver.onChanged(StateChart.OnConnectToWebSocket)
                stateObserver.onChanged(StateChart.StopLoading)
                stateObserver.onChanged(StateChart.ShowErrorMessage(errorMessage))
            }
        }
    }

    @Test
    fun `viewModel state will change to BlankState after setBlankState is called`() {

        every { networkConnectivityManager.isConnected() } returns false

        val viewModel = ChartViewModel(chartUseCases, networkConnectivityManager, mapper)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.setBlankState()

            verifyOrder {
                stateObserver.onChanged(StateChart.BlankState)
            }
        }
    }
}