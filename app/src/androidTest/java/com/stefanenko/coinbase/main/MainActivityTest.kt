package com.stefanenko.coinbase.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.activity.login.LoginActivity
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    var activityScenario = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun main_activity_navigation(){
        onView(withId(R.id.chart)).perform(click())
        onView(withId(R.id.chart_fragment_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.filter)).check(matches(isDisplayed()))

        pressBack()
        onView(withId(R.id.filter)).check(doesNotExist())
    }
}