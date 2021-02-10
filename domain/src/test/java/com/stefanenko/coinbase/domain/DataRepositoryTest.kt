package com.stefanenko.coinbase.domain

import com.BaseDomainModuleTest
import com.google.common.truth.Truth.assertThat
import com.stefanenko.coinbase.data.database.entity.ExchangeRateEntity
import com.stefanenko.coinbase.data.database.entity.FavoriteExchangeRatesEntity
import com.stefanenko.coinbase.data.network.dto.activeCurrency.ActiveCurrencyResponse
import com.stefanenko.coinbase.data.network.dto.exchange.ResponseExchangerRates
import com.stefanenko.coinbase.data.network.dto.profile.ResponseProfile
import com.stefanenko.coinbase.data.service.DatabaseService
import com.stefanenko.coinbase.data.service.RemoteDataService
import com.stefanenko.coinbase.domain.di.DaggerDomainComponentTest
import com.stefanenko.coinbase.domain.entity.ActiveCurrency
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.Profile
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.util.mapper.Mapper
import com.stefanenko.coinbase.domain.repository.DataRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest

import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DataRepositoryTest: BaseDomainModuleTest() {

    init {
        component.inject(this)
    }

    @Inject
    lateinit var remoteDataService: RemoteDataService

    @Inject
    lateinit var databaseService: DatabaseService

    @Inject
    lateinit var mapper: Mapper

    @Test
    fun `getExchangeRates returns ResponseState Data with list of ExchangeRate if locale list is empty`() {

        val responseExchangerRates: ResponseExchangerRates = mockk()
        val exchangerRate = ExchangeRate("USD", "BTC", 22.4, "data", "time")

        coEvery { databaseService.getExchangeRateList() } returns emptyList()
        coEvery { remoteDataService.getExchangeRates(any()) } returns responseExchangerRates
        every { mapper.map(responseExchangerRates) } returns listOf(exchangerRate)
        every { mapper.mapToExchangeRateEntity(exchangerRate) } returns mockk()
        coEvery { databaseService.updateExchangeRateList(any()) } returns true

        val dataRepository = DataRepository(remoteDataService, databaseService, mapper)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val response = dataRepository.getExchangeRates("USD")
            assertThat(response).isInstanceOf(ResponseState.Data::class.java)
            assertThat((response as ResponseState.Data).data[0]).isEqualTo(exchangerRate)
        }
    }

    @Test
    fun `getExchangeRates returns ResponseState Data with list of ExchangeRate from database`() {

        val exchangeRateEntity: ExchangeRateEntity = mockk()
        val exchangeRateEntityList: List<ExchangeRateEntity> = listOf(exchangeRateEntity)
        val exchangerRate = ExchangeRate("USD", "BTC", 22.4, "data", "time")

        coEvery { databaseService.getExchangeRateList() } returns exchangeRateEntityList
        every { mapper.map(exchangeRateEntity)} returns exchangerRate

        val dataRepository = DataRepository(remoteDataService, databaseService, mapper)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val response = dataRepository.getExchangeRates("USD")
            assertThat(response).isInstanceOf(ResponseState.Data::class.java)
            assertThat((response as ResponseState.Data).data[0]).isEqualTo(exchangerRate)
        }
    }

    @Test
    fun `getExchangeRates returns ResponseState Error if remote service threw an Exception`() {

        coEvery { databaseService.getExchangeRateList() } returns emptyList()
        coEvery { remoteDataService.getExchangeRates(any()) } throws  Exception("getExchangeRates: test Exception")

        val dataRepository = DataRepository(remoteDataService, databaseService, mapper)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val error = dataRepository.getExchangeRates("USD")
            assertThat(error).isInstanceOf(ResponseState.Error::class.java)
        }
    }

    @Test
    fun `updateExchangeRates returns ResponseState Data with list of ExchangeRate`() {

        val responseExchangerRates: ResponseExchangerRates = mockk()
        val exchangerRate = ExchangeRate("USD", "BTC", 22.4, "data", "time")

        coEvery { remoteDataService.getExchangeRates(any()) } returns responseExchangerRates
        every { mapper.map(responseExchangerRates) } returns listOf(exchangerRate)
        every { mapper.mapToExchangeRateEntity(exchangerRate) } returns mockk()
        coEvery { databaseService.updateExchangeRateList(any()) } returns true

        val dataRepository = DataRepository(remoteDataService, databaseService, mapper)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val response = dataRepository.updateExchangeRates("USD")
            assertThat(response).isInstanceOf(ResponseState.Data::class.java)
            assertThat((response as ResponseState.Data).data[0]).isEqualTo(exchangerRate)
        }
    }

    @Test
    fun `updateExchangeRates returns ResponseState Error if remote service threw an Exception`() {

        coEvery { remoteDataService.getExchangeRates(any()) } throws  Exception("updateExchangeRates: test Exception")

        val dataRepository = DataRepository(remoteDataService, databaseService, mapper)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val error = dataRepository.updateExchangeRates("USD")
            assertThat(error).isInstanceOf(ResponseState.Error::class.java)
        }
    }

    @Test
    fun `getProfile returns ResponseState Data with Profile`() {

        val responseProfile: ResponseProfile = mockk()
        val profile = Profile("name", "email", "imageUrl", "countryName")

        coEvery { remoteDataService.getProfile(any()) } returns responseProfile
        every { mapper.map(responseProfile) } returns profile

        val dataRepository = DataRepository(remoteDataService, databaseService, mapper)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val response = dataRepository.getProfile("token")
            assertThat(response).isInstanceOf(ResponseState.Data::class.java)
            assertThat((response as ResponseState.Data).data).isEqualTo(profile)
        }
    }

    @Test
    fun `getProfile returns ResponseState Error if remote service threw an Exception`() {

        coEvery { remoteDataService.getProfile(any()) } throws Exception("getProfile: test exception")

        val dataRepository = DataRepository(remoteDataService, databaseService, mapper)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val error = dataRepository.getProfile("token")
            assertThat(error).isInstanceOf(ResponseState.Error::class.java)
        }
    }

    @Test
    fun `getActiveCurrency returns ResponseState Data with list of ActiveCurrency`() {

        val activeCurrencyResponse: ActiveCurrencyResponse = mockk()
        val activeCurrencyResponseList: List<ActiveCurrencyResponse> = listOf(activeCurrencyResponse)
        val activeCurrency = ActiveCurrency("USD")

        coEvery { remoteDataService.getActiveCurrency() } returns activeCurrencyResponseList
        every { mapper.map(activeCurrencyResponse) } returns activeCurrency

        val dataRepository = DataRepository(remoteDataService, databaseService, mapper)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val response = dataRepository.getActiveCurrency()
            assertThat(response).isInstanceOf(ResponseState.Data::class.java)
            assertThat((response as ResponseState.Data).data[0]).isEqualTo(activeCurrency)
        }
    }

    @Test
    fun `getActiveCurrency returns ResponseState Error if remote service threw an Exception`() {

        coEvery { remoteDataService.getActiveCurrency() } throws Exception("getActiveCurrency: test exception")

        val dataRepository = DataRepository(remoteDataService, databaseService, mapper)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val response = dataRepository.getActiveCurrency()
            assertThat(response).isInstanceOf(ResponseState.Error::class.java)
        }
    }

    @Test
    fun `getFavorites returns ResponseState ResponseState Data with list of ExchangeRate`() {

        val exchangeRateEntity: ExchangeRateEntity = mockk()
        val exchangeRateEntityList: List<ExchangeRateEntity> = listOf(exchangeRateEntity)
        val exchangerRate = ExchangeRate("USD", "BTC", 22.4, "data", "time")

        coEvery { databaseService.getFavorites() } returns exchangeRateEntityList
        every { mapper.map(exchangeRateEntity)} returns exchangerRate

        val dataRepository = DataRepository(remoteDataService, databaseService, mapper)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val response = dataRepository.getFavorites()
            assertThat(response).isInstanceOf(ResponseState.Data::class.java)
            assertThat((response as ResponseState.Data).data[0]).isEqualTo(exchangerRate)
        }
    }

    @Test
    fun `deleteExchangeRateFromFavorites returns ResponseState Data(true) if service delete method returns true`(){

        val exchangeRate: ExchangeRate = mockk()
        val exchangerRateFav: FavoriteExchangeRatesEntity = mockk()

        coEvery { databaseService.deleteExchangeRateFromFavorite(exchangerRateFav) } returns true
        every { mapper.mapToFavoriteExchangeRateEntity(exchangeRate) } returns exchangerRateFav

        val dataRepository = DataRepository(remoteDataService, databaseService, mapper)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val response = dataRepository.deleteExchangeRateFromFavorites(exchangeRate)
            assertThat(response).isInstanceOf(ResponseState.Data::class.java)
            assertThat((response as ResponseState.Data).data).isEqualTo(true)
        }
    }

    @Test
    fun `deleteExchangeRateFromFavorites returns ResponseState Error if service delete method returns false`(){

        val exchangeRate: ExchangeRate = mockk()
        val exchangerRateFav: FavoriteExchangeRatesEntity = mockk()

        coEvery { databaseService.deleteExchangeRateFromFavorite(exchangerRateFav) } returns false
        every { mapper.mapToFavoriteExchangeRateEntity(exchangeRate) } returns exchangerRateFav

        val dataRepository = DataRepository(remoteDataService, databaseService, mapper)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val response = dataRepository.deleteExchangeRateFromFavorites(exchangeRate)
            assertThat(response).isInstanceOf(ResponseState.Error::class.java)
        }
    }

    @Test
    fun `addExchangeRateToFavorites returns ResponseState Data(true) if service add method returns true`(){

        val exchangeRate: ExchangeRate = mockk()
        val exchangerRateFav: FavoriteExchangeRatesEntity = mockk()

        coEvery { databaseService.addExchangeRateToFavorite(exchangerRateFav) } returns true
        every { mapper.mapToFavoriteExchangeRateEntity(exchangeRate) } returns exchangerRateFav

        val dataRepository = DataRepository(remoteDataService, databaseService, mapper)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val response = dataRepository.addExchangeRateToFavorite(exchangeRate)
            assertThat(response).isInstanceOf(ResponseState.Data::class.java)
            assertThat((response as ResponseState.Data).data).isEqualTo(true)
        }
    }

    @Test
    fun `addExchangeRateToFavorites return ResponseState Error if service add method returns false`(){

        val exchangeRate: ExchangeRate = mockk()
        val exchangerRateFav: FavoriteExchangeRatesEntity = mockk()

        coEvery { databaseService.addExchangeRateToFavorite(exchangerRateFav) } returns false
        every { mapper.mapToFavoriteExchangeRateEntity(exchangeRate) } returns exchangerRateFav

        val dataRepository = DataRepository(remoteDataService, databaseService, mapper)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val response = dataRepository.addExchangeRateToFavorite(exchangeRate)
            assertThat(response).isInstanceOf(ResponseState.Error::class.java)
        }
    }

    @Test
    fun `getCashedExchangeRates returns ResponseState Data(list of ExchangeRate)`(){

        val exchangeRateEntity: ExchangeRateEntity = mockk()
        val exchangeRateEntityList: List<ExchangeRateEntity> = listOf(exchangeRateEntity)
        val exchangerRate = ExchangeRate("USD", "BTC", 22.4, "data", "time")

        coEvery { databaseService.getExchangeRateList() } returns exchangeRateEntityList
        every { mapper.map(exchangeRateEntity) } returns exchangerRate

        val dataRepository = DataRepository(remoteDataService, databaseService, mapper)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val response = dataRepository.getCashedExchangeRates()
            assertThat(response).isInstanceOf(ResponseState.Data::class.java)
            assertThat((response as ResponseState.Data).data[0]).isEqualTo(exchangerRate)
        }
    }
}