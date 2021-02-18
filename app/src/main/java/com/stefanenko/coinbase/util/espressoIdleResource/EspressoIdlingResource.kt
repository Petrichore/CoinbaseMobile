package com.stefanenko.coinbase.util.espressoIdleResource

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {

    private val COUNTING_IDLING_RESOURCE_NAME = "IDLING_RESOURCE"

    val countingIdlingResource = CountingIdlingResource(COUNTING_IDLING_RESOURCE_NAME)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}