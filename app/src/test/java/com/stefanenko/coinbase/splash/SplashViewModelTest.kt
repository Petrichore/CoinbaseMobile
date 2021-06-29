package com.stefanenko.coinbase.splash

import androidx.lifecycle.Observer
import com.google.common.truth.Truth
import com.stefanenko.coinbase.BaseAppModuleTest
import com.stefanenko.coinbase.di.DaggerAppComponentTest
import com.stefanenko.coinbase.ui.activity.login.StateLogin
import com.stefanenko.coinbase.ui.activity.splash.SplashActivityState
import com.stefanenko.coinbase.ui.activity.splash.SplashViewModel
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SplashViewModelTest : BaseAppModuleTest() {

    init {
        component.inject(this)
    }

    @Inject
    lateinit var authPref: AuthPreferences

    private val stateObserver: Observer<SplashActivityState> = mockk()

    @Before
    fun setUp() {
        every { stateObserver.onChanged(any()) } returns Unit
    }

    @Test
    fun `viewModel state if user already auth`() {
        every { authPref.isUserAuth() } returns true
        every { authPref.getAccessToken() } returns "access token test"
        every { authPref.getRefreshToken() } returns "refresh token test"

        val viewModel = SplashViewModel(authPref)
        viewModel.state.observeForever(stateObserver)

        viewModel.checkUserAuth()

        verifyOrder {
            stateObserver.onChanged(SplashActivityState.UserIsAuth)
        }
    }

    @Test
    fun `viewModel state if user didn't auth`() {
        every { authPref.isUserAuth() } returns false
        every { authPref.getAccessToken() } returns "access token test"
        every { authPref.getRefreshToken() } returns "refresh token test"

        val viewModel = SplashViewModel(authPref)
        viewModel.state.observeForever(stateObserver)

        viewModel.checkUserAuth()

        verifyOrder {
            stateObserver.onChanged(SplashActivityState.OpenLoginActivity)
        }
    }
}