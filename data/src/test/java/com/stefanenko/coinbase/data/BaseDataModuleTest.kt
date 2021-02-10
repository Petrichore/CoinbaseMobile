package com.stefanenko.coinbase.data

import android.util.Log
import com.stefanenko.coinbase.data.di.DaggerDataComponentTest
import com.stefanenko.coinbase.data.di.DataComponentTest
import com.stefanenko.coinbase.data.di.DataTestModule
import com.stefanenko.coinbase.data.util.coroutineDispatcher.CoroutinesTestRule
import com.stefanenko.coinbase.data.util.coroutineDispatcher.TestDispatchersProvider
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import javax.inject.Inject

@ExperimentalCoroutinesApi
abstract class BaseDataModuleTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    protected val component: DataComponentTest

    init {
        component = DaggerDataComponentTest.builder()
            .dataTestModule(DataTestModule(coroutinesTestRule))
            .build()
        component.injectBase(this)
        setUpLog()
    }

    @Inject
    lateinit var coroutineTestDispatcher: TestDispatchersProvider

    private fun setUpLog() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.v(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
    }
}