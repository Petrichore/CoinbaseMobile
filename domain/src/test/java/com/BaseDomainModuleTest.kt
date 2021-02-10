package com

import android.util.Log
import com.stefanenko.coinbase.domain.di.DaggerDomainComponentTest
import com.stefanenko.coinbase.domain.di.DomainComponentTest
import com.stefanenko.coinbase.domain.util.CoroutinesTestRule
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class BaseDomainModuleTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    protected val component: DomainComponentTest

    init {
        component = DaggerDomainComponentTest.builder().build()
        setUpLog()
    }

    private fun setUpLog() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.v(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
    }
}