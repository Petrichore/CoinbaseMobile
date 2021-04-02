package com.stefanenko.coinbase.profile

import androidx.lifecycle.Observer
import com.stefanenko.coinbase.BaseAppModuleTest
import com.stefanenko.coinbase.domain.entity.Profile
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.exception.ERROR_UNAUTHORIZED
import com.stefanenko.coinbase.domain.useCase.ProfileUseCases
import com.stefanenko.coinbase.ui.fragment.exchangeRate.ExchangeRatesViewModel
import com.stefanenko.coinbase.ui.fragment.exchangeRate.StateExchangeRates
import com.stefanenko.coinbase.ui.fragment.profile.ProfileViewModel
import com.stefanenko.coinbase.ui.fragment.profile.StateProfile
import com.stefanenko.coinbase.util.errorHandler.ViewModelErrorHandler
import com.stefanenko.coinbase.util.exception.ERROR_INTERNET_CONNECTION
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import com.stefanenko.coinbase.util.preferences.AuthPreferences
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
class ProfileViewModelTest : BaseAppModuleTest() {

    init {
        component.inject(this)
    }

    @Inject
    lateinit var authPref: AuthPreferences

    @Inject
    lateinit var connectivityManager: NetworkConnectivityManager

    @Inject
    lateinit var profileUseCases: ProfileUseCases

    @Inject
    lateinit var viewModelErrorHandler: ViewModelErrorHandler

    private val stateObserver: Observer<StateProfile> = mockk()

    @Before
    fun setUp() {
        every { stateObserver.onChanged(any()) } returns Unit
    }

    @Test
    fun `viewModel state when getProfile and user is authed and network connection exists`() {
        val accessToken = "token"
        val userProfile: Profile = mockk()
        val response: ResponseState<Profile> = ResponseState.Data(userProfile)

        every { authPref.isUserAuth() } returns true
        every { connectivityManager.isConnected() } returns true
        every { authPref.getAccessToken() } returns accessToken

        coEvery { profileUseCases.getProfile(accessToken) } returns response




        val viewModel =
            ProfileViewModel(profileUseCases, connectivityManager, authPref, viewModelErrorHandler)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.getProfile()

            verifyOrder {
                stateObserver.onChanged(StateProfile.StartLoading)
                stateObserver.onChanged(StateProfile.ShowProfileData(userProfile))
                stateObserver.onChanged(StateProfile.StopLoading)
            }
        }
    }

    @Test
    fun `viewModel state when getProfile request returns ERROR_UNAUTHORIZED and reauth successfully`() {
        val accessToken = "access token"
        val refreshToken = "refresh token"
        val newAccessToken = "new access token"
        val newRefreshToken = "new refresh token"

        val response: ResponseState<Profile> = ResponseState.Error(ERROR_UNAUTHORIZED)

        every { authPref.isUserAuth() } returns true
        every { connectivityManager.isConnected() } returns true
        every { authPref.getAccessToken() } returns accessToken
        every { authPref.getRefreshToken() } returns refreshToken

        coEvery { profileUseCases.getProfile(accessToken) } returns response
        coEvery { viewModelErrorHandler.handleUnauthorizedError(any(), any()) } answers {
            (this.invocation.args[0] as (Pair<String, String>) -> Unit).invoke(
                Pair(
                    newAccessToken,
                    newRefreshToken
                )
            )
        }

        every { authPref.saveAccessToken(newAccessToken) } returns Unit
        every { authPref.saveRefreshToken(newRefreshToken) } returns Unit

        val viewModel =
            ProfileViewModel(profileUseCases, connectivityManager, authPref, viewModelErrorHandler)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.getProfile()

            verifyOrder {
                stateObserver.onChanged(StateProfile.StartLoading)
                stateObserver.onChanged(StateProfile.ReAuthPerformed)
                stateObserver.onChanged(StateProfile.StopLoading)
            }
        }
    }

    @Test
    fun `viewModel state when getProfile request returns ERROR_UNAUTHORIZED and reauth failed`() {
        val accessToken = "access token"
        val refreshToken = "refresh token"
        val reauthErrorMessage = "reauth error message test"

        val response: ResponseState<Profile> = ResponseState.Error(ERROR_UNAUTHORIZED)

        every { authPref.isUserAuth() } returns true
        every { connectivityManager.isConnected() } returns true
        every { authPref.getAccessToken() } returns accessToken
        every { authPref.getRefreshToken() } returns refreshToken

        coEvery { profileUseCases.getProfile(accessToken) } returns response
        coEvery { viewModelErrorHandler.handleUnauthorizedError(any(), any()) } answers {
            (this.invocation.args[1] as (String) -> Unit).invoke(reauthErrorMessage)
        }

        val viewModel =
            ProfileViewModel(profileUseCases, connectivityManager, authPref, viewModelErrorHandler)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.getProfile()

            verifyOrder {
                stateObserver.onChanged(StateProfile.StartLoading)
                stateObserver.onChanged(StateProfile.ShowErrorMessage(reauthErrorMessage))
                stateObserver.onChanged(StateProfile.StopLoading)
            }
        }
    }

    @Test
    fun `viewModel state when getProfile request returns unknown error`() {
        val accessToken = "access token"
        val refreshToken = "refresh token"
        val unknownErrorMessage = "unknown error message"

        val response: ResponseState<Profile> = ResponseState.Error(unknownErrorMessage)

        every { authPref.isUserAuth() } returns true
        every { connectivityManager.isConnected() } returns true
        every { authPref.getAccessToken() } returns accessToken
        every { authPref.getRefreshToken() } returns refreshToken

        coEvery { profileUseCases.getProfile(accessToken) } returns response

        val viewModel =
            ProfileViewModel(profileUseCases, connectivityManager, authPref, viewModelErrorHandler)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.getProfile()

            verifyOrder {
                stateObserver.onChanged(StateProfile.StartLoading)
                stateObserver.onChanged(StateProfile.ShowErrorMessage(unknownErrorMessage))
                stateObserver.onChanged(StateProfile.StopLoading)
            }
        }
    }

    @Test
    fun `viewModel state when user is authed but network is lack`() {

        every { authPref.isUserAuth() } returns true
        every { connectivityManager.isConnected() } returns false

        val viewModel =
            ProfileViewModel(profileUseCases, connectivityManager, authPref, viewModelErrorHandler)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.getProfile()

            verifyOrder {
                stateObserver.onChanged(StateProfile.ShowErrorMessage(ERROR_INTERNET_CONNECTION))
            }
        }
    }

    @Test
    fun `viewModel state is GuestMode if user isn't authed yet`() {

        every { authPref.isUserAuth() } returns false

        val viewModel =
            ProfileViewModel(profileUseCases, connectivityManager, authPref, viewModelErrorHandler)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.getProfile()

            verifyOrder {
                stateObserver.onChanged(StateProfile.GuestMode)
            }
        }
    }

    @Test
    fun `viewModel state if logOut performed`() {

        every { authPref.clearLoginSate() } returns Unit

        val viewModel =
            ProfileViewModel(profileUseCases, connectivityManager, authPref, viewModelErrorHandler)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.performLogout()

            verifyOrder {
                stateObserver.onChanged(StateProfile.LogOut)
            }
        }
    }

    @Test
    fun `viewModel state will change to BlankState after setBlankState is called`() {
        every { connectivityManager.isConnected() } returns false

        val viewModel =
            ProfileViewModel(profileUseCases, connectivityManager, authPref, viewModelErrorHandler)
        viewModel.state.observeForever(stateObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.setBlankState()

            verifyOrder {
                stateObserver.onChanged(StateProfile.BlankState)
            }
        }
    }

}