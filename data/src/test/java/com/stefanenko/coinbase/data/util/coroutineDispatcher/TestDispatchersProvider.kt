package com.stefanenko.coinbase.data.util.coroutineDispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class TestDispatchersProvider(private val coroutineTestRule: CoroutinesTestRule) :
    BaseCoroutineDispatcher {

    override fun io(): CoroutineDispatcher {
        return coroutineTestRule.testDispatcher
    }

    override fun default(): CoroutineDispatcher {
        return coroutineTestRule.testDispatcher
    }

    override fun main(): CoroutineDispatcher {
        return coroutineTestRule.testDispatcher
    }
}