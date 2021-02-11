package com.stefanenko.coinbase.exchangeRate

import androidx.lifecycle.Observer
import com.stefanenko.coinbase.BaseAppModuleTest
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.useCase.ExchangeRateUseCases
import com.stefanenko.coinbase.domain.useCase.FavoritesUseCases
import com.stefanenko.coinbase.ui.fragment.exchangeRate.ExchangeRatesViewModel
import com.stefanenko.coinbase.ui.fragment.exchangeRate.StateExchangeRates
import com.stefanenko.coinbase.util.exception.ERROR_INTERNET_CONNECTION
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import javax.inject.Inject


@ExperimentalCoroutinesApi
class ExchangeRateViewModelTest : BaseAppModuleTest() {

    init {
        component.inject(this)
    }

    @Inject
    lateinit var exchangeRateUseCases: ExchangeRateUseCases

    @Inject
    lateinit var favoriteUseCases: FavoritesUseCases

    @Inject
    lateinit var authPref: AuthPreferences

    @Inject
    lateinit var networkConnectivityManager: NetworkConnectivityManager

    private val stateObserver: Observer<StateExchangeRates> = mockk()

    @Before
    fun setUp() {
        every { stateObserver.onChanged(any()) } returns Unit
    }

    @Test
    fun `viewModel state if getExchangeRates performed successfully and network connection exists`() {
        val baseCurrency = "USD"
        val expectedResponse: ResponseState.Data<List<ExchangeRate>> = ResponseState.Data(listOf())
        every { networkConnectivityManager.isConnected() } returns true
        every { networkConnectivityManager.regNetworkCallBack(any(), any()) } returns mockk()
        coEvery { exchangeRateUseCases.getExchangeRates(baseCurrency) } returns expectedResponse

        val viewModel = ExchangeRatesViewModel(
            exchangeRateUseCases,
            favoriteUseCases,
            authPref,
            networkConnectivityManager
        )
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.getExchangeRates(baseCurrency)

            verifyAll {
                stateObserver.onChanged(StateExchangeRates.StartLoading)
                stateObserver.onChanged(StateExchangeRates.ShowExchangeRateRecycler(expectedResponse.data))
                stateObserver.onChanged(StateExchangeRates.StopLoading)
            }
        }
    }

    @Test
    fun `viewModel state if network connection is lack and getCachedExchangeRates performed successfully`() {
        val baseCurrency = "USD"
        val expectedResponse: ResponseState.Data<List<ExchangeRate>> = ResponseState.Data(listOf())

        every { networkConnectivityManager.isConnected() } returns false
        every { networkConnectivityManager.regNetworkCallBack(any(), any()) } returns mockk()
        coEvery { exchangeRateUseCases.getCashedExchangeRates() } returns expectedResponse

        val viewModel = ExchangeRatesViewModel(
            exchangeRateUseCases,
            favoriteUseCases,
            authPref,
            networkConnectivityManager
        )
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.getExchangeRates(baseCurrency)

            verifyOrder {
                stateObserver.onChanged(StateExchangeRates.StartLoading)
                stateObserver.onChanged(StateExchangeRates.ShowExchangeRateRecycler(expectedResponse.data))
                stateObserver.onChanged(StateExchangeRates.StopLoading)
            }
        }
    }

    @Test
    fun `viewModel state if getExchangeRates returns error and network connection exists`() {
        val baseCurrency = "USD"
        val errorMessage = "getExchangeRates test error"
        val expectedResponse: ResponseState.Error<List<ExchangeRate>> =
            ResponseState.Error(errorMessage)

        every { networkConnectivityManager.isConnected() } returns true
        every { networkConnectivityManager.regNetworkCallBack(any(), any()) } returns mockk()
        coEvery { exchangeRateUseCases.getExchangeRates(baseCurrency) } returns expectedResponse

        val viewModel = ExchangeRatesViewModel(
            exchangeRateUseCases,
            favoriteUseCases,
            authPref,
            networkConnectivityManager
        )
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.getExchangeRates(baseCurrency)

            verifyOrder {
                stateObserver.onChanged(StateExchangeRates.StartLoading)
                stateObserver.onChanged(StateExchangeRates.ShowErrorMessage(expectedResponse.error))
                stateObserver.onChanged(StateExchangeRates.StopLoading)
            }
        }
    }


    @Test
    fun `viewModel state if getExchangeRates returns error and network connection connection is lack`() {
        val baseCurrency = "USD"
        val errorMessage = "getExchangeRates test error"
        val expectedResponse: ResponseState.Error<List<ExchangeRate>> =
            ResponseState.Error(errorMessage)

        every { networkConnectivityManager.isConnected() } returns false
        every { networkConnectivityManager.regNetworkCallBack(any(), any()) } returns mockk()
        coEvery { exchangeRateUseCases.getCashedExchangeRates() } returns expectedResponse

        val viewModel = ExchangeRatesViewModel(
            exchangeRateUseCases,
            favoriteUseCases,
            authPref,
            networkConnectivityManager
        )
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.getExchangeRates(baseCurrency)

            verifyOrder {
                stateObserver.onChanged(StateExchangeRates.StartLoading)
                stateObserver.onChanged(StateExchangeRates.ShowErrorMessage(expectedResponse.error))
                stateObserver.onChanged(StateExchangeRates.StopLoading)
            }
        }
    }

    @Test
    fun `viewModel state if updateExchangeRates performed successfully`() {
        val baseCurrency = "USD"
        val expectedResponse: ResponseState.Data<List<ExchangeRate>> = ResponseState.Data(listOf())

        every { networkConnectivityManager.isConnected() } returns true
        every { networkConnectivityManager.regNetworkCallBack(any(), any()) } returns mockk()
        coEvery { exchangeRateUseCases.updateExchangeRates(baseCurrency) } returns expectedResponse

        val viewModel = ExchangeRatesViewModel(
            exchangeRateUseCases,
            favoriteUseCases,
            authPref,
            networkConnectivityManager
        )
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.updateExchangeRates(baseCurrency)

            verifyOrder {
                stateObserver.onChanged(StateExchangeRates.StartLoading)
                stateObserver.onChanged(StateExchangeRates.UpdateExchangeRateRecycler(expectedResponse.data))
                stateObserver.onChanged(StateExchangeRates.StopLoading)
            }
        }
    }

    @Test
    fun `viewModel state if updateExchangeRates returns error`() {
        val baseCurrency = "USD"
        val expectedResponse: ResponseState.Error<List<ExchangeRate>> =
            ResponseState.Error("updateExchangeRates test error")

        every { networkConnectivityManager.isConnected() } returns true
        every { networkConnectivityManager.regNetworkCallBack(any(), any()) } returns mockk()
        coEvery { exchangeRateUseCases.updateExchangeRates(baseCurrency) } returns expectedResponse

        val viewModel = ExchangeRatesViewModel(
            exchangeRateUseCases,
            favoriteUseCases,
            authPref,
            networkConnectivityManager
        )
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.updateExchangeRates(baseCurrency)

            verifyOrder {
                stateObserver.onChanged(StateExchangeRates.StartLoading)
                stateObserver.onChanged(StateExchangeRates.ShowErrorMessage(expectedResponse.error))
                stateObserver.onChanged(StateExchangeRates.StopLoading)
            }
        }
    }

    @Test
    fun `viewModel state if updateExchangeRates not performed due to lack of internet connection`() {
        val baseCurrency = "USD"
        val expectedResponse: ResponseState.Error<List<ExchangeRate>> =
            ResponseState.Error(ERROR_INTERNET_CONNECTION)

        every { networkConnectivityManager.isConnected() } returns false
        every { networkConnectivityManager.regNetworkCallBack(any(), any()) } returns mockk()
        coEvery { exchangeRateUseCases.updateExchangeRates(baseCurrency) } returns expectedResponse

        val viewModel = ExchangeRatesViewModel(
            exchangeRateUseCases,
            favoriteUseCases,
            authPref,
            networkConnectivityManager
        )
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.updateExchangeRates(baseCurrency)

            verifyOrder {
                stateObserver.onChanged(StateExchangeRates.StartLoading)
                stateObserver.onChanged(StateExchangeRates.ShowErrorMessage(expectedResponse.error))
                stateObserver.onChanged(StateExchangeRates.StopLoading)
            }
        }
    }

    @Test
    fun `viewModel state if addToFavorite performed successfully`() {
        val fakeExchangeRate: ExchangeRate = mockk()
        val expectedResponse: ResponseState.Data<Boolean> = ResponseState.Data(true)

        every { networkConnectivityManager.isConnected() } returns false
        every { networkConnectivityManager.regNetworkCallBack(any(), any()) } returns mockk()
        coEvery { favoriteUseCases.addFavorite(fakeExchangeRate) } returns expectedResponse

        val viewModel = ExchangeRatesViewModel(
            exchangeRateUseCases,
            favoriteUseCases,
            authPref,
            networkConnectivityManager
        )
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.addCurrencyToFavorite(fakeExchangeRate)

            verifyOrder {
                stateObserver.onChanged(StateExchangeRates.ShowSnackBar)
            }
        }
    }

    @Test
    fun `viewModel state if addToFavorite returns error`() {
        val fakeExchangeRate: ExchangeRate = mockk()
        val errorMessage = "addToFavorite test error"
        val expectedResponse: ResponseState.Error<Boolean> = ResponseState.Error(errorMessage)

        every { networkConnectivityManager.isConnected() } returns false
        every { networkConnectivityManager.regNetworkCallBack(any(), any()) } returns mockk()
        coEvery { favoriteUseCases.addFavorite(fakeExchangeRate) } returns expectedResponse

        val viewModel = ExchangeRatesViewModel(
            exchangeRateUseCases,
            favoriteUseCases,
            authPref,
            networkConnectivityManager
        )
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.addCurrencyToFavorite(fakeExchangeRate)

            verifyOrder {
                stateObserver.onChanged(StateExchangeRates.ShowErrorMessage(errorMessage))
            }
        }
    }

    @Test
    fun `viewModel state if checkAbilityToSaveFavorites and user is auth`() {
        val fakeExchangeRate: ExchangeRate = mockk()

        every { networkConnectivityManager.isConnected() } returns false
        every { networkConnectivityManager.regNetworkCallBack(any(), any()) } returns mockk()
        coEvery { authPref.isUserAuth() } returns true

        val viewModel = ExchangeRatesViewModel(
            exchangeRateUseCases,
            favoriteUseCases,
            authPref,
            networkConnectivityManager
        )
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.checkAbilityToSaveCurrency(fakeExchangeRate)

            verifyOrder {
                stateObserver.onChanged(StateExchangeRates.ShowDialogSaveToFav(fakeExchangeRate))
            }
        }
    }

    @Test
    fun `viewModel state if checkAbilityToSaveFavorites and user isn't auth yet`() {
        val fakeExchangeRate: ExchangeRate = mockk()

        every { networkConnectivityManager.isConnected() } returns false
        every { networkConnectivityManager.regNetworkCallBack(any(), any()) } returns mockk()
        coEvery { authPref.isUserAuth() } returns true

        val viewModel = ExchangeRatesViewModel(
            exchangeRateUseCases,
            favoriteUseCases,
            authPref,
            networkConnectivityManager
        )
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.checkAbilityToSaveCurrency(fakeExchangeRate)

            verifyOrder {
                stateObserver.onChanged(StateExchangeRates.ShowDialogSaveToFav(fakeExchangeRate))
            }
        }
    }

    @Test
    fun `viewModel state will change to BlankState after setBlankState is called`() {
        every { networkConnectivityManager.isConnected() } returns false
        every { networkConnectivityManager.regNetworkCallBack(any(), any()) } returns mockk()

        val viewModel = ExchangeRatesViewModel(
            exchangeRateUseCases,
            favoriteUseCases,
            authPref,
            networkConnectivityManager
        )
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.setBlankState()

            verifyOrder {
                stateObserver.onChanged(StateExchangeRates.BlankState)
            }
        }
    }
}