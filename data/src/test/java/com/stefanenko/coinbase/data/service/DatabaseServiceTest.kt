package com.stefanenko.coinbase.data.service

import com.google.common.truth.Truth.assertThat
import com.stefanenko.coinbase.data.database.dao.CurrencyDao
import com.stefanenko.coinbase.data.database.entity.ExchangeRateEntity
import com.stefanenko.coinbase.data.di.DaggerDataComponentTest
import com.stefanenko.coinbase.data.di.DataTestModule
import com.stefanenko.coinbase.data.service.DatabaseService
import com.stefanenko.coinbase.data.util.coroutineDispatcher.BaseCoroutineDispatcher
import com.stefanenko.coinbase.data.util.coroutineDispatcher.CoroutinesTestRule
import com.stefanenko.coinbase.data.util.coroutineDispatcher.TestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DatabaseServiceTest {

    init {
        val component =
            DaggerDataComponentTest.builder().dataTestModule(DataTestModule()).build()
        component.inject(this)
    }

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Inject
    lateinit var currencyDao: CurrencyDao

    private val coroutineTestDispatcher = TestDispatchersProvider(coroutinesTestRule)

    @Test
    fun `addExchangeRateToFavorite returns true if id greater than 0`() {
        val id = 64L
        coEvery { currencyDao.addCurrencyExchangeRateToFavorite(any()) } returns id
        val databaseService = DatabaseService(currencyDao, coroutineTestDispatcher)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val isAddedSuccessfully = databaseService.addExchangeRateToFavorite(mockk())
            assertThat(isAddedSuccessfully).isEqualTo(true)
        }
    }

    @Test
    fun `addExchangeRateToFavorite returns false if id less than 0`() {
        val id = 0L
        coEvery { currencyDao.addCurrencyExchangeRateToFavorite(any()) } returns id
        val databaseService = DatabaseService(currencyDao, coroutineTestDispatcher)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val isAddedSuccessfully = databaseService.addExchangeRateToFavorite(mockk())
            assertThat(isAddedSuccessfully).isEqualTo(false)
        }
    }

    @Test
    fun `getExchangeRateList returns list of ExchangeRateEntity`() {
        val exchangeRateEntity = ExchangeRateEntity("name", "baseCurrency", 22.4, "data", "time")
        coEvery { currencyDao.getExchangeRates() } returns listOf(exchangeRateEntity)
        val databaseService = DatabaseService(currencyDao, coroutineTestDispatcher)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val exchangeRateEntityList = databaseService.getExchangeRateList()
            assertThat(exchangeRateEntityList[0]).isEqualTo(exchangeRateEntity)
        }
    }

    @Test
    fun `deleteExchangeRateFromFavorite returns true if amount of deleted items greater than 0`() {
        val deletedAmount = 1
        coEvery { currencyDao.deleteExchangeRateFromFavorite(any()) } returns deletedAmount
        val databaseService = DatabaseService(currencyDao, coroutineTestDispatcher)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val isDeleted = databaseService.deleteExchangeRateFromFavorite(mockk())
            assertThat(isDeleted).isEqualTo(true)
        }
    }

    @Test
    fun `deleteExchangeRateFromFavorite returns false if amount of deleted items less than 0`() {
        val deletedAmount = 0
        coEvery { currencyDao.deleteExchangeRateFromFavorite(any()) } returns deletedAmount
        val databaseService = DatabaseService(currencyDao, coroutineTestDispatcher)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val isDeleted = databaseService.deleteExchangeRateFromFavorite(mockk())
            assertThat(isDeleted).isEqualTo(false)
        }
    }

    @Test
    fun `getFavorites returns list of ExchangeRateEntity`() {
        val exchangeRateEntity = ExchangeRateEntity("name", "baseCurrency", 22.4, "data", "time")
        coEvery { currencyDao.getFavoritesData() } returns listOf(exchangeRateEntity)
        val databaseService = DatabaseService(currencyDao, coroutineTestDispatcher)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val exchangeRateEntityList = databaseService.getFavorites()
            assertThat(exchangeRateEntityList[0]).isEqualTo(exchangeRateEntity)
        }
    }

    @Test
    fun `updateExchangeRateList returns true if items updated successfully`() {
        val exchangeRateEntityList: List<ExchangeRateEntity> = listOf(mockk())
        coEvery { currencyDao.updateExchangeRateTable(exchangeRateEntityList) } returns true
        val databaseService = DatabaseService(currencyDao, coroutineTestDispatcher)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val isUpdatedSuccessfully =
                databaseService.updateExchangeRateList(exchangeRateEntityList)
            assertThat(isUpdatedSuccessfully).isTrue()
        }
    }

    @Test
    fun `updateExchangeRateList throws an Exception if error occurs while items updating`() {
        val exceptionMessage = "updateExchangeRateList: TestException"
        val exchangeRateEntityList: List<ExchangeRateEntity> = listOf(mockk())
        coEvery { currencyDao.updateExchangeRateTable(exchangeRateEntityList) } throws Exception(
            exceptionMessage
        )
        val databaseService = DatabaseService(currencyDao, coroutineTestDispatcher)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            try {
                val isUpdatedSuccessfully =
                    databaseService.updateExchangeRateList(exchangeRateEntityList)
                assertThat(isUpdatedSuccessfully).isTrue()
                assertThat(false).isTrue()
            } catch (e: Exception) {
                assertThat(true).isTrue()
            }
        }
    }
}