package com.stefanenko.coinbase.fragment.chart.chartFilter

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.fragment.chart.chartFilter.CurrencyFilterFragment
import com.stefanenko.coinbase.util.espressoIdleResource.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CurrencyCurrencyFilterFragmentTest {

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun reset() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun chartFilter_shows_availableCurrencyRecycler(){
        launchFragmentInContainer<CurrencyFilterFragment>(null, R.style.Theme_CoinbaseMobile)

        onView(withId(R.id.activeCurrencyRecycler)).check(matches(isDisplayed()))
    }
}