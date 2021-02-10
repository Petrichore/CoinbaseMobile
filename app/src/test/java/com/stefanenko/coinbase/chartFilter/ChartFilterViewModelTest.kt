package com.stefanenko.coinbase.chartFilter

import androidx.lifecycle.Observer
import com.stefanenko.coinbase.BaseAppModuleTest
import com.stefanenko.coinbase.domain.entity.ActiveCurrency
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.useCase.ChartFilterUseCases
import com.stefanenko.coinbase.ui.fragment.chart.chartFilter.FilterViewModel
import com.stefanenko.coinbase.ui.fragment.chart.chartFilter.StateFilter
import com.stefanenko.coinbase.util.exception.ERROR_INTERNET_CONNECTION
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ChartFilterViewModelTest : BaseAppModuleTest() {

    init {
        component.inject(this)
    }

    @Inject
    lateinit var chartFilterUseCases: ChartFilterUseCases

    @Inject
    lateinit var connectivityManager: NetworkConnectivityManager

    private val stateObserver: Observer<StateFilter> = mockk()

    @Before
    fun setUp() {
        every { stateObserver.onChanged(any()) } returns Unit
    }

    @Test
    fun `viewModel state if getActiveCurrency performed successfully and network connection exists`() {
        val activeCurrency: ActiveCurrency = mockk()
        val dataList = listOf(activeCurrency)
        val responseStateData = ResponseState.Data(dataList)

        every { connectivityManager.isConnected() } returns true
        coEvery { chartFilterUseCases.getActiveCurrency() } returns responseStateData

        val viewMode = FilterViewModel(chartFilterUseCases, connectivityManager)
        viewMode.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewMode.getActiveCurrency()

            verifyOrder {
                stateObserver.onChanged(StateFilter.StartLoading)
                stateObserver.onChanged(StateFilter.ShowCurrencyRecycler(dataList))
                stateObserver.onChanged(StateFilter.StopLoading)
            }
        }
    }

    @Test
    fun `viewModel state if network connection is lack`() {
        every { connectivityManager.isConnected() } returns false

        val viewMode = FilterViewModel(chartFilterUseCases, connectivityManager)
        viewMode.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewMode.getActiveCurrency()

            verifyOrder {
                stateObserver.onChanged(StateFilter.ShowErrorMessage(ERROR_INTERNET_CONNECTION))
            }
        }
    }

    @Test
    fun `viewModel state when getActiveCurrency return error and network connection exists`() {
        val errorMessage = "getActiveCurrency test error"
        val responseStateData: ResponseState.Error<List<ActiveCurrency>> = ResponseState.Error(errorMessage)

        every { connectivityManager.isConnected() } returns true
        coEvery { chartFilterUseCases.getActiveCurrency() } returns responseStateData

        val viewMode = FilterViewModel(chartFilterUseCases, connectivityManager)
        viewMode.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewMode.getActiveCurrency()

            verifyOrder {
                stateObserver.onChanged(StateFilter.StartLoading)
                stateObserver.onChanged(StateFilter.ShowErrorMessage(errorMessage))
                stateObserver.onChanged(StateFilter.StopLoading)
            }
        }
    }
}