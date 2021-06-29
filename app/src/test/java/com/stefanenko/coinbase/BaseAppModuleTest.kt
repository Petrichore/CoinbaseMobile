package com.stefanenko.coinbase

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.stefanenko.coinbase.di.AppComponentTest
import com.stefanenko.coinbase.di.DaggerAppComponentTest
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class BaseAppModuleTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    protected val component: AppComponentTest = DaggerAppComponentTest.builder().build()

    init {
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