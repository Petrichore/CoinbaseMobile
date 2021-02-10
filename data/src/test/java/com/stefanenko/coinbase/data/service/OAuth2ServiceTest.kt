package com.stefanenko.coinbase.data.service

import com.google.common.truth.Truth.assertThat
import com.stefanenko.coinbase.data.BaseDataModuleTest
import com.stefanenko.coinbase.data.network.api.AuthApi
import com.stefanenko.coinbase.data.network.dto.token.RequestAccessToken
import com.stefanenko.coinbase.data.network.dto.token.RequestRefreshToken
import com.stefanenko.coinbase.data.network.dto.token.RequestRevokeToken
import com.stefanenko.coinbase.data.network.dto.token.ResponseAccessToken
import com.stefanenko.coinbase.data.util.NetworkResponseHandler
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import retrofit2.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
class OAuth2ServiceTest: BaseDataModuleTest() {

    init {
        component.inject(this)
    }

    @Inject
    lateinit var responseHandler: NetworkResponseHandler

    @Inject
    lateinit var authApi: AuthApi

    @Test
    fun `getAccessToken returns ResponseAccessToken if Response was successful`() {
        val requestAccessToken =
            RequestAccessToken("id", "secret", "grantType", "redirectUri", "authCode")
        val expectedResponseAccessToken =
            ResponseAccessToken("accessToken", "refreshToken", "tokenType", "scope", 64, 1024)
        val response = Response.success(expectedResponseAccessToken)

        coEvery { authApi.getAccessToken(requestAccessToken) } returns response
        every { responseHandler.handleResponse(response) } returns expectedResponseAccessToken

        val oAuth2Service =
            OAuth2Service(authApi, responseHandler, coroutineTestDispatcher)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val responseAccessToken = oAuth2Service.getAccessToken(requestAccessToken)
            assertThat(responseAccessToken).isEqualTo(expectedResponseAccessToken)
        }
    }

    @Test
    fun `getAccessToken throws an Exception if Response was failed`() {
        val exceptionMessage = "getAccessToken: exception test message"
        val requestAccessToken: RequestAccessToken = mockk()
        val response: Response<ResponseAccessToken> = mockk()

        coEvery { authApi.getAccessToken(requestAccessToken) } returns response
        every { responseHandler.handleResponse(response) } throws Exception(exceptionMessage)

        val oAuth2Service =
            OAuth2Service(authApi, responseHandler, coroutineTestDispatcher)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            try {
                oAuth2Service.getAccessToken(requestAccessToken)
                assertThat(false).isTrue()
            } catch (e: Exception) {
                assertThat(true).isTrue()
            }
        }
    }

    @Test
    fun `refreshToken returns ResponseAccessToken if Response was successful`() {
        val requestRefreshToken =
            RequestRefreshToken("id", "secret", "grantType", "redirectUri")
        val expectedResponseAccessToken =
            ResponseAccessToken("accessToken", "refreshToken", "tokenType", "scope", 64, 1024)
        val response = Response.success(expectedResponseAccessToken)

        coEvery { authApi.refreshToken(requestRefreshToken) } returns response
        every { responseHandler.handleResponse(response) } returns expectedResponseAccessToken

        val oAuth2Service =
            OAuth2Service(authApi, responseHandler, coroutineTestDispatcher)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val responseAccessToken = oAuth2Service.refreshToken(requestRefreshToken)
            assertThat(responseAccessToken).isEqualTo(expectedResponseAccessToken)
        }
    }

    @Test
    fun `refreshToken throws an Exception if Response was failed`() {
        val exceptionMessage = "refreshToken: exception test message"
        val requestRefreshToken: RequestRefreshToken = mockk()
        val response: Response<ResponseAccessToken> = mockk()

        coEvery { authApi.refreshToken(requestRefreshToken) } returns response
        every { responseHandler.handleResponse(response) } throws Exception(exceptionMessage)

        val oAuth2Service =
            OAuth2Service(authApi, responseHandler, coroutineTestDispatcher)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            try {
                oAuth2Service.refreshToken(requestRefreshToken)
                assertThat(false).isTrue()
            } catch (e: Exception) {
                assertThat(true).isTrue()
            }
        }
    }

    @Test
    fun `revokeToken returns true if Response was successful`() {
        val requestRefreshToken =
            RequestRevokeToken("Bearer:token")
        val bearerToken = "token"

        val responseAny: Any = mockk()
        val response = Response.success(responseAny)

        coEvery { authApi.revokeToken(requestRefreshToken, bearerToken) } returns response
        every { responseHandler.handleResponse(response) } returns responseAny

        val oAuth2Service =
            OAuth2Service(authApi, responseHandler, coroutineTestDispatcher)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            val isRevokedSuccessfully = oAuth2Service.revokeToken(requestRefreshToken, bearerToken)
            assertThat(isRevokedSuccessfully).isTrue()
        }
    }

    @Test
    fun `revokeToken throws an Exception if Response was failed`() {
        val exceptionMessage = "revokeToken: exception test message"
        val requestRevokeToken: RequestRevokeToken = mockk()
        val bearerToken = "token"
        val response: Response<Any> = mockk()

        coEvery { authApi.revokeToken(requestRevokeToken, bearerToken) } returns response
        every { responseHandler.handleResponse(response) } throws Exception(exceptionMessage)

        val oAuth2Service =
            OAuth2Service(authApi, responseHandler, coroutineTestDispatcher)

        coroutinesTestRule.testDispatcher.runBlockingTest {
            try {
                oAuth2Service.revokeToken(requestRevokeToken, bearerToken)
                assertThat(false).isTrue()
            } catch (e: Exception) {
                assertThat(true).isTrue()
            }
        }
    }
}