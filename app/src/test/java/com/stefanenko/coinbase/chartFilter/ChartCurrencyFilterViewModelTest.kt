package com.stefanenko.coinbase.chartFilter

import android.text.TextUtils
import androidx.lifecycle.Observer
import com.stefanenko.coinbase.BaseAppModuleTest
import com.stefanenko.coinbase.domain.entity.ActiveCurrency
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.useCase.ChartFilterUseCases
import com.stefanenko.coinbase.ui.fragment.chart.chartFilter.CurrencyFilterViewModel
import com.stefanenko.coinbase.ui.fragment.chart.chartFilter.StateCurrencyFilter
import com.stefanenko.coinbase.util.espressoIdleResource.EspressoIdlingResource
import com.stefanenko.coinbase.util.exception.ERROR_INTERNET_CONNECTION
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ChartCurrencyFilterViewModelTest : BaseAppModuleTest() {

    init {
        component.inject(this)

        mockkStatic(TextUtils::class)
        every { TextUtils.isEmpty(any()) } returns false

        mockkObject(EspressoIdlingResource)
        every { EspressoIdlingResource.increment() } returns Unit
        every { EspressoIdlingResource.decrement() } returns Unit
    }

    @Inject
    lateinit var chartFilterUseCases: ChartFilterUseCases

    @Inject
    lateinit var connectivityManager: NetworkConnectivityManager

    private val stateCurrencyObserver: Observer<StateCurrencyFilter> = mockk()

    @Before
    fun setUp() {
        every { stateCurrencyObserver.onChanged(any()) } returns Unit
    }

    @Test
    fun `viewModel state if getActiveCurrency performed successfully and network connection exists`() {
        val activeCurrency: ActiveCurrency = mockk()
        val dataList = listOf(activeCurrency)
        val responseStateData = ResponseState.Data(dataList)

        every { connectivityManager.isConnected() } returns true
        coEvery { chartFilterUseCases.getActiveCurrency() } returns responseStateData

        val viewMode = CurrencyFilterViewModel(chartFilterUseCases, connectivityManager)
        viewMode.state.observeForever(stateCurrencyObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewMode.getActiveCurrency()

            verifyOrder {
                stateCurrencyObserver.onChanged(StateCurrencyFilter.StartLoading)
                stateCurrencyObserver.onChanged(StateCurrencyFilter.ShowCurrencyRecycler(dataList))
                stateCurrencyObserver.onChanged(StateCurrencyFilter.StopLoading)
            }
        }
    }

    @Test
    fun `viewModel state if network connection is lack`() {
        every { connectivityManager.isConnected() } returns false

        val viewMode = CurrencyFilterViewModel(chartFilterUseCases, connectivityManager)
        viewMode.state.observeForever(stateCurrencyObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewMode.getActiveCurrency()

            verifyOrder {
                stateCurrencyObserver.onChanged(StateCurrencyFilter.ShowErrorMessage(ERROR_INTERNET_CONNECTION))
            }
        }
    }

    @Test
    fun `viewModel state when getActiveCurrency return error and network connection exists`() {
        val errorMessage = "getActiveCurrency test error"
        val responseStateData: ResponseState.Error<List<ActiveCurrency>> = ResponseState.Error(errorMessage)

        every { connectivityManager.isConnected() } returns true
        coEvery { chartFilterUseCases.getActiveCurrency() } returns responseStateData

        val viewMode = CurrencyFilterViewModel(chartFilterUseCases, connectivityManager)
        viewMode.state.observeForever(stateCurrencyObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewMode.getActiveCurrency()

            verifyOrder {
                stateCurrencyObserver.onChanged(StateCurrencyFilter.StartLoading)
                stateCurrencyObserver.onChanged(StateCurrencyFilter.ShowErrorMessage(errorMessage))
                stateCurrencyObserver.onChanged(StateCurrencyFilter.StopLoading)
            }
        }
    }
}