package com.stefanenko.coinbase.util.viewModelErrorHandler

import com.google.common.truth.Truth
import com.stefanenko.coinbase.BaseAppModuleTest
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.useCase.AuthUseCases
import com.stefanenko.coinbase.util.errorHandler.ViewModelErrorHandler
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import javax.inject.Inject


@ExperimentalCoroutinesApi
class ViewModelErrorHandlerTest : BaseAppModuleTest() {

    init {
        component.inject(this)
    }

    @Inject
    lateinit var authUseCases: AuthUseCases

    @Inject
    lateinit var authPref: AuthPreferences

    @Test
    fun `handle unauthorized error successfully`() {
        val refreshToken = "refresh token test"
        val newAccessToken = "new access token"
        val newRefreshToken = "new refresh token"
        val refreshedTokenPair = Pair(newAccessToken, newRefreshToken)
        val response: ResponseState<Pair<String, String>> = ResponseState.Data(refreshedTokenPair)

        every { authPref.getRefreshToken() } returns refreshToken
        coEvery { authUseCases.refreshToken(refreshToken) } returns response

        val viewModelErrorHandler = ViewModelErrorHandler(authUseCases, authPref)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModelErrorHandler.handleUnauthorizedError({
                Truth.assertThat(it).isEqualTo(refreshedTokenPair)
            }, {
                Truth.assertThat(true).isFalse()
            })
        }
    }

    @Test
    fun `handle unauthorized error failed`() {
        val refreshToken = "refresh token test"

        val refreshingTokenError= "refreshing token error"
        val response: ResponseState<Pair<String, String>> = ResponseState.Error(refreshingTokenError)

        every { authPref.getRefreshToken() } returns refreshToken
        coEvery { authUseCases.refreshToken(refreshToken) } returns response

        val viewModelErrorHandler = ViewModelErrorHandler(authUseCases, authPref)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            viewModelErrorHandler.handleUnauthorizedError({
                Truth.assertThat(true).isFalse()
            }, {
                Truth.assertThat(it).isEqualTo(refreshingTokenError)
            })
        }
    }
}