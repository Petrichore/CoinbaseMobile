package com.stefanenko.coinbase.fragment.exchangeRate

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.fragment.exchangeRate.ExchangeRatesFragment
import com.stefanenko.coinbase.ui.fragment.exchangeRate.recycler.CurrencyExchangeRateViewHolder
import com.stefanenko.coinbase.util.espressoIdleResource.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ExchangeRatesFragmentTest{

    @Before
    fun setUp(){

        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun reset(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun exchangeRate_recycler_shows_data() {
        launchFragmentInContainer<ExchangeRatesFragment>(null, R.style.Theme_CoinbaseMobile)

        onView(withId(R.id.exchangeRatesRecycler)).check(matches(isDisplayed()))
    }

    @Test
    fun currency_exchange_rate_card_long_click_shows_dialog() {
        launchFragmentInContainer<ExchangeRatesFragment>(null, R.style.Theme_CoinbaseMobile)

        onView(withId(R.id.exchangeRatesRecycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CurrencyExchangeRateViewHolder>(
                0,
                longClick()
            )
        )
        onView(withText(R.string.alert_dialog_title_add_to_favorite)).check(matches(isDisplayed()))
    }
}