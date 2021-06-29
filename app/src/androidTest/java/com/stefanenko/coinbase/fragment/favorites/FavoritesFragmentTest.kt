package com.stefanenko.coinbase.fragment.favorites

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.fragment.exchangeRate.recycler.CurrencyExchangeRateViewHolder
import com.stefanenko.coinbase.ui.fragment.favorites.FavoritesFragment
import com.stefanenko.coinbase.util.espressoIdleResource.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class FavoritesFragmentTest {

    @Before
    fun setUp(){

        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun reset(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun favorites_recycler_view() {
        val fragmentScenario =
            launchFragmentInContainer<FavoritesFragment>(null, R.style.Theme_CoinbaseMobile)

        var isUserAuth = false
        fragmentScenario.onFragment { fragment ->
            isUserAuth = fragment.viewModel.authPreferences.isUserAuth()
        }

        if (isUserAuth) {
            onView(withId(R.id.fragment_favorites_layout)).check(matches(isDisplayed()))
            onView(withId(R.id.favoritesRecycler)).check(matches(isDisplayed()))
            onView(withId(R.id.favoritesRecycler)).perform(
                RecyclerViewActions.actionOnItemAtPosition<CurrencyExchangeRateViewHolder>(
                    0,
                    ViewActions.swipeRight()
                )
            )
            onView(withText(R.string.snackBar_text_exchange_rate_removed)).check(matches(isDisplayed()))
            onView(withText(R.string.snackBar_text_undo_remove)).check(matches(isDisplayed()))

            onView(withText(R.string.snackBar_text_undo_remove)).perform(ViewActions.click())
            //onView(withId(R.id.favoritesRecycler)).check(matches(isDisplayed()))
        } else {
            onView(withId(R.id.fragment_favorites_guest_mode)).check(matches(isDisplayed()))
            onView(withId(R.id.toAuthBtn)).perform(ViewActions.click())
            onView(withId(R.id.activity_login_layout)).check(matches(isDisplayed()))
        }
    }
}