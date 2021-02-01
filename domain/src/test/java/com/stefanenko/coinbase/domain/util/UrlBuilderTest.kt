package com.stefanenko.coinbase.domain.util

import com.google.common.truth.Truth.assertThat
import com.stefanenko.coinbase.domain.util.UrlBuilder
import org.junit.Test

class UrlBuilderTest {

    @Test
    fun `buildUrl with empty pass and params returns baseUrl`() {
        val urlBuilder = UrlBuilder()
        val baseUrl = "baseUrl"

        val url = urlBuilder.buildUrl(baseUrl)

        assertThat(url).isEqualTo(baseUrl)
    }

    @Test
    fun `buildUrl with params and empty pass returns baseUrl with params`() {
        val urlBuilder = UrlBuilder()
        val baseUrl = "baseUrl"

        val url = urlBuilder.buildUrl(baseUrl)

        assertThat(url).isEqualTo(baseUrl)
    }
}