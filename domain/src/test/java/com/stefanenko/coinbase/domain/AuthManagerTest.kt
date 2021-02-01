package com.stefanenko.coinbase.domain

import android.net.Uri
import com.google.common.truth.Truth.assertThat
import com.stefanenko.coinbase.data.service.OAuth2Service
import com.stefanenko.coinbase.domain.di.DaggerDomainComponentTest
import com.stefanenko.coinbase.domain.repository.AuthManager
import com.stefanenko.coinbase.domain.util.UrlBuilder
import com.stefanenko.coinbase.domain.util.oAuthScope.ScopeBuilder
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Test
import javax.inject.Inject

class AuthManagerTest {

    @Inject
    lateinit var oAuth2Service: OAuth2Service

    @Inject
    lateinit var scopeBuilder: ScopeBuilder

    @Inject
    lateinit var urlBuilder: UrlBuilder

    init {
        DaggerDomainComponentTest.builder().build().inject(this)
    }

    @Test
    fun `start auth`() {

        val createdUrl = "url"

        every { scopeBuilder.build(*anyVararg()) } returns "scope"
        every { urlBuilder.buildUrl(any(), *anyVararg(), any()) } returns createdUrl
        every { Uri.parse(createdUrl)} returns mockk()

        val authManager = AuthManager(oAuth2Service, scopeBuilder, urlBuilder)
        val uri = authManager.startAuth()

        assertThat(uri.toString()).isEqualTo(createdUrl)
    }
}