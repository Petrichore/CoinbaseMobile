package com.stefanenko.coinbase.activity.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var activityScenario = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun main_navigation_to_chart(){
        onView(withId(R.id.chart)).perform(click())
        onView(withId(R.id.fragment_chart_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.filter)).check(matches(isDisplayed()))

        pressBack()
        onView(withId(R.id.filter)).check(doesNotExist())
        onView(withId(R.id.fragment_exchange_rate_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun main_navigation_to_favorites(){
        onView(withId(R.id.favorites)).perform(click())
        onView(withId(R.id.fragment_favorites_layout)).check(matches(isDisplayed()))

        pressBack()
        onView(withId(R.id.fragment_exchange_rate_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun main_navigation_to_profile(){
        onView(withId(R.id.profile)).perform(click())
        onView(withId(R.id.fragment_profile_layout)).check(matches(isDisplayed()))

        pressBack()
        onView(withId(R.id.fragment_exchange_rate_layout)).check(matches(isDisplayed()))
    }
}