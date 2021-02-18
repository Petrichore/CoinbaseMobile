package com.stefanenko.coinbase.favorites

import android.text.TextUtils
import androidx.lifecycle.Observer
import com.google.common.truth.Truth
import com.stefanenko.coinbase.BaseAppModuleTest
import com.stefanenko.coinbase.di.DaggerAppComponentTest
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.useCase.FavoritesUseCases
import com.stefanenko.coinbase.ui.fragment.favorites.FavoritesViewModel
import com.stefanenko.coinbase.ui.fragment.favorites.StateFavorites
import com.stefanenko.coinbase.util.espressoIdleResource.EspressoIdlingResource
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FavoritesViewModelTest : BaseAppModuleTest() {

    init {
        component.inject(this)

        mockkStatic(TextUtils::class)
        every { TextUtils.isEmpty(any()) } returns false

        mockkObject(EspressoIdlingResource)
        every { EspressoIdlingResource.increment() } returns Unit
        every { EspressoIdlingResource.decrement() } returns Unit
    }

    @Inject
    lateinit var favoritesUseCases: FavoritesUseCases

    @Inject
    lateinit var authPref: AuthPreferences

    private val stateObserver: Observer<StateFavorites> = mockk()

    @Before
    fun setUp() {
        every { stateObserver.onChanged(any()) } returns Unit
    }

    @Test
    fun `viewModel state if getFavorites performed successfully and user is auth`() {

        val fakeExchangeRateList: List<ExchangeRate> = mockk()
        val expectedResponse: ResponseState.Data<List<ExchangeRate>> =
            ResponseState.Data(fakeExchangeRateList)

        every { authPref.isUserAuth() } returns true
        coEvery { favoritesUseCases.getFavorites() } returns expectedResponse

        val viewModel = FavoritesViewModel(favoritesUseCases, authPref)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.getFavorites()

            verifyOrder {
                stateObserver.onChanged(StateFavorites.StartLoading)
                stateObserver.onChanged(StateFavorites.ShowFavoritesRecycler(expectedResponse.data))
                stateObserver.onChanged(StateFavorites.StopLoading)
            }
        }
    }

    @Test
    fun `viewModel state if getFavorites failed and user is auth`() {

        val errorMessage = "getFavorites test error"
        val expectedResponse: ResponseState.Error<List<ExchangeRate>> =
            ResponseState.Error(errorMessage)

        every { authPref.isUserAuth() } returns true
        coEvery { favoritesUseCases.getFavorites() } returns expectedResponse

        val viewModel = FavoritesViewModel(favoritesUseCases, authPref)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.getFavorites()

            verifyOrder {
                stateObserver.onChanged(StateFavorites.StartLoading)
                stateObserver.onChanged(StateFavorites.ShowErrorMessage(expectedResponse.error))
                stateObserver.onChanged(StateFavorites.StopLoading)
            }
        }
    }

    @Test
    fun `viewModel state when try to getFavorites but user isn't auth yet`() {

        every { authPref.isUserAuth() } returns false

        val viewModel = FavoritesViewModel(favoritesUseCases, authPref)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.getFavorites()

            verifyOrder {
                stateObserver.onChanged(StateFavorites.GuestMode)
            }
        }
    }

    @Test
    fun `viewModel state when intent to delete item`() {
        val deleteItemPosition = 1
        val fakeExchangeRate: ExchangeRate = mockk()

        val viewModel = FavoritesViewModel(favoritesUseCases, authPref)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.itemDeleteIntent(deleteItemPosition, fakeExchangeRate)

            verifyOrder {
                stateObserver.onChanged(StateFavorites.ItemRemoveIntent(deleteItemPosition))
                stateObserver.onChanged(StateFavorites.ShowRetrieveItemSnackBar)
            }
        }
    }

    @Test
    fun `viewModel state when cancel item delete`() {
        val deleteItemPosition = 1
        val deletingItem: ExchangeRate = mockk()

        val viewModel = FavoritesViewModel(favoritesUseCases, authPref)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.itemDeleteIntent(deleteItemPosition, deletingItem)
            viewModel.cancelItemDelete()

            verifyOrder {
                stateObserver.onChanged(StateFavorites.CancelItemDelete(deleteItemPosition, deletingItem))
            }
        }
    }

    @Test
    fun `viewModel state when performing delete`() {
        val deleteItemPosition = 1
        val deletingItem: ExchangeRate = mockk()
        val responseState = ResponseState.Data(true)

        coEvery {favoritesUseCases.deleteFavorite(deletingItem)} returns responseState

        val viewModel = FavoritesViewModel(favoritesUseCases, authPref)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.itemDeleteIntent(deleteItemPosition, deletingItem)
            viewModel.performDelete()
            verifyOrder {
                stateObserver.onChanged(StateFavorites.DeleteCompleted)
            }
        }
    }

    @Test
    fun `viewModel state after setBlankState call`() {

        val viewModel = FavoritesViewModel(favoritesUseCases, authPref)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.setBlankState()

            verifyOrder {
                stateObserver.onChanged(StateFavorites.BlankState)
            }
        }
    }
}