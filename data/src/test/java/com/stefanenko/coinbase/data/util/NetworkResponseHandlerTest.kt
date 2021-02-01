package com.stefanenko.coinbase.data.util

import com.google.common.truth.Truth.assertThat
import com.stefanenko.coinbase.data.exception.EXCEPTION_NOT_FOUND
import com.stefanenko.coinbase.data.util.NetworkResponseHandler
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import retrofit2.Response
import java.lang.Exception

class NetworkResponseHandlerTest {

    @Test
    fun `success response returns body`() {
        val responseSuccess: Response<String> = mockk()
        val body = "body"

        every { responseSuccess.isSuccessful } returns true
        every { responseSuccess.body() } returns body

        val networkResponseHandler = NetworkResponseHandler()
        val response = networkResponseHandler.handleResponse(responseSuccess)
        assertThat(response).isEqualTo(body)
    }

    @Test
    fun `fail response with code 404 returns exception with right message`() {
        val responseFail: Response<String> = mockk()
        val messageFor404Code = EXCEPTION_NOT_FOUND

        every { responseFail.isSuccessful } returns false
        every { responseFail.code() } returns 404

        val networkResponseHandler = NetworkResponseHandler()

        try{
            networkResponseHandler.handleResponse(responseFail)
            assertThat(false).isTrue()
        }catch (e: Exception){
            assertThat(e.message).isEqualTo(messageFor404Code)
        }
    }
}