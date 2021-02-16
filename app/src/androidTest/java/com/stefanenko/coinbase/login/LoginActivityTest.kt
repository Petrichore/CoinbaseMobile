package com.stefanenko.coinbase.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.activity.login.LoginActivity
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    var mainActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun click_on_quickStart_btn_open_MainActivity() {
        onView(withId(R.id.quickStartBtn)).perform(click())
        onView((withId(R.id.activity_main_layout))).check(matches(isDisplayed()))
        onView((withId(R.id.fragment_exchange_rate_layout))).check(matches(isDisplayed()))
    }
}