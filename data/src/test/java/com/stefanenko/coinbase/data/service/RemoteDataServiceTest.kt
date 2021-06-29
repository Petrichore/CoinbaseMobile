package com.stefanenko.coinbase.data.service


import com.stefanenko.coinbase.data.network.api.BitmexMarketApi
import com.stefanenko.coinbase.data.network.api.CoinbaseMarketApi
import com.stefanenko.coinbase.data.network.api.CoinbaseProfileApi
import com.stefanenko.coinbase.data.network.dto.DefaultResponse
import com.stefanenko.coinbase.data.network.dto.exchange.ResponseExchangerRates
import com.stefanenko.coinbase.data.network.dto.profile.ProfileCountry
import com.stefanenko.coinbase.data.network.dto.profile.ResponseProfile
import com.stefanenko.coinbase.data.util.NetworkResponseHandler
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import retrofit2.Response
import javax.inject.Inject
import com.google.common.truth.Truth.assertThat
import com.stefanenko.coinbase.data.BaseDataModuleTest
import com.stefanenko.coinbase.data.network.dto.activeCurrency.ActiveCurrencyResponse
import io.mockk.mockk
import kotlin.Exception

@ExperimentalCoroutinesApi
class RemoteDataServiceTest: BaseDataModuleTest() {

    init {
        component.inject(this)
    }

    @Inject
    lateinit var coinbaseMarketApi: CoinbaseMarketApi

    @Inject
    lateinit var coinbaseProfileApi: CoinbaseProfileApi

    @Inject
    lateinit var bitmexApi: BitmexMarketApi

    @Inject
    lateinit var responseHandler: NetworkResponseHandler

    @Test
    fun `getExchangeRates returns data if response is success`() {
        val exchangeRateDefaultResponse = DefaultResponse(ResponseExchangerRates("USD", mapOf()))
        val response = Response.success(exchangeRateDefaultResponse)

        coEvery { coinbaseMarketApi.getExchangeRatesRequest(any()) } returns response
        every { responseHandler.handleResponse(response) } returns exchangeRateDefaultResponse

        val remoteDataService =
            RemoteDataService(
                coinbaseMarketApi,
                coinbaseProfileApi,
                bitmexApi,
                responseHandler,
                coroutineTestDispatcher
            )

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val serviceResponse = remoteDataService.getExchangeRates("USD")
            assertThat(serviceResponse).isEqualTo(exchangeRateDefaultResponse.data)
        }
    }

    @Test
    fun `getExchangeRates throws an Exception if response is fail`() {
        val exceptionMessage = "getExchangeRates: Test exception"
        coEvery { coinbaseMarketApi.getExchangeRatesRequest(any()) } returns mockk()
        every { responseHandler.handleResponse(any<Response<DefaultResponse<ResponseExchangerRates>>>()) } throws Exception(exceptionMessage)

        val remoteDataService =
            RemoteDataService(
                coinbaseMarketApi,
                coinbaseProfileApi,
                bitmexApi,
                responseHandler,
                coroutineTestDispatcher
            )

        coroutinesTestRule.testDispatcher.runBlockingTest {
            try {
                remoteDataService.getExchangeRates("USD")
                assertThat(false).isTrue()
            } catch (e: Exception) {
                assertThat(e.message).isEqualTo(exceptionMessage)
            }
        }
    }

    @Test
    fun `get user profile`() {
        val profileDefaultResponse = DefaultResponse(
            ResponseProfile(
                "Ivan",
                "email@gmail.com",
                "url",
                ProfileCountry("BY", "Belarus", "true")
            )
        )
        val successResponse = Response.success(profileDefaultResponse)

        coEvery { coinbaseProfileApi.getProfile("token") } returns successResponse
        every { responseHandler.handleResponse(successResponse) } returns profileDefaultResponse

        val remoteDataService =
            RemoteDataService(
                coinbaseMarketApi,
                coinbaseProfileApi,
                bitmexApi,
                responseHandler,
                coroutineTestDispatcher
            )

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val serviceResponse = remoteDataService.getProfile("token")
            assertThat(serviceResponse).isEqualTo(profileDefaultResponse.data)
        }
    }

    @Test
    fun `get active currency list`() {
        val activeCurrencyListResponse = listOf<ActiveCurrencyResponse>()
        val successResponse = Response.success(activeCurrencyListResponse)

        coEvery { bitmexApi.getActiveCurrency() } returns successResponse
        every { responseHandler.handleResponse(successResponse) } returns activeCurrencyListResponse

        val remoteDataService =
            RemoteDataService(
                coinbaseMarketApi,
                coinbaseProfileApi,
                bitmexApi,
                responseHandler,
                coroutineTestDispatcher
            )

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val serviceResponse = remoteDataService.getActiveCurrency()
            assertThat(serviceResponse).isEqualTo(activeCurrencyListResponse)
        }
    }
}