package com.stefanenko.coinbase.login

import android.net.Uri
import androidx.lifecycle.Observer
import com.stefanenko.coinbase.BaseAppModuleTest
import com.stefanenko.coinbase.di.DaggerAppComponentTest
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.useCase.AuthUseCases
import com.stefanenko.coinbase.ui.activity.login.LoginViewModel
import com.stefanenko.coinbase.ui.activity.login.StateLogin
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
class LoginViewModelTest : BaseAppModuleTest() {

    init {
        component.inject(this)
    }

    @Inject
    lateinit var authUseCases: AuthUseCases

    @Inject
    lateinit var authPref: AuthPreferences

    @Inject
    lateinit var networkConnectivityManager: NetworkConnectivityManager

    private val stateLoginObserver: Observer<StateLogin> = mockk()

    @Before
    fun setUp() {
        every { stateLoginObserver.onChanged(any()) } returns Unit
    }

    @Test
    fun `performAuth set OpenCoinbaseAuth state if internet connection exists`() {
        val fakeUri: Uri = mockk()

        every { networkConnectivityManager.isConnected() } returns true
        every { authUseCases.startAuth() } returns fakeUri

        val viewModel = LoginViewModel(authPref, authUseCases, networkConnectivityManager)
        viewModel.state.observeForever(stateLoginObserver)

        viewModel.performAuth()

        verifyOrder {
            stateLoginObserver.onChanged(StateLogin.OpenCoinbaseAuthPage(fakeUri))
        }
    }

    @Test
    fun `performAuth set ShowErrorMessage(no internet connection) state if internet connection doesn't exist`() {
        val fakeUri: Uri = mockk()

        every { networkConnectivityManager.isConnected() } returns false
        every { authUseCases.startAuth() } returns fakeUri

        val viewModel = LoginViewModel(authPref, authUseCases, networkConnectivityManager)
        viewModel.state.observeForever(stateLoginObserver)

        viewModel.performAuth()

        verifyOrder {
            stateLoginObserver.onChanged(StateLogin.ShowErrorMessage(ERROR_INTERNET_CONNECTION))
        }
    }

    @Test
    fun `viewModel state if auth completed successfully`() {
        val fakeUri: Uri = mockk()
        val expectedResponseState: ResponseState.Data<Pair<String, String>> =
            ResponseState.Data(Pair("first", "second"))

        every { fakeUri.toString() } returns "uriStr"
        coEvery { authPref.saveAccessToken(any()) } returns Unit
        coEvery { authPref.saveRefreshToken(any()) } returns Unit
        coEvery { authPref.setUserAuthState(any()) } returns Unit
        coEvery { authUseCases.completeAuth(any()) } returns expectedResponseState

        val viewModel = LoginViewModel(authPref, authUseCases, networkConnectivityManager)
        viewModel.state.observeForever(stateLoginObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.completeAuth(fakeUri)
        }

        verifyOrder {
            stateLoginObserver.onChanged(StateLogin.StartLoading)
            stateLoginObserver.onChanged(StateLogin.AuthCompleted)
            stateLoginObserver.onChanged(StateLogin.StopLoading)
        }
    }

    @Test
    fun `viewModel state if auth completion fails`() {
        val errorMessage = "complete auth error"
        val fakeUri: Uri = mockk()
        val expectedResponseState: ResponseState.Error<Pair<String, String>> =
            ResponseState.Error(errorMessage)

        every { fakeUri.toString() } returns "uriStr"
        coEvery { authPref.saveAccessToken(any()) } returns Unit
        coEvery { authPref.saveRefreshToken(any()) } returns Unit
        coEvery { authPref.setUserAuthState(any()) } returns Unit
        coEvery { authUseCases.completeAuth(any()) } returns expectedResponseState

        val viewModel = LoginViewModel(authPref, authUseCases, networkConnectivityManager)
        viewModel.state.observeForever(stateLoginObserver)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModel.completeAuth(fakeUri)
        }

        verifyOrder {
            stateLoginObserver.onChanged(StateLogin.StartLoading)
            stateLoginObserver.onChanged(StateLogin.ShowErrorMessage(errorMessage))
            stateLoginObserver.onChanged(StateLogin.StopLoading)
        }
    }
}