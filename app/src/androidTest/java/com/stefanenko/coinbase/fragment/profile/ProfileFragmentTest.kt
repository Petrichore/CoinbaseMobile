package com.stefanenko.coinbase.fragment.profile


import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.fragment.profile.ProfileFragment
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ProfileFragmentTest {

    @Test
    fun profile_info() {
        val fragmentScenario =
            launchFragmentInContainer<ProfileFragment>(null, R.style.Theme_CoinbaseMobile)

        var isUserAuth = false
        fragmentScenario.onFragment { fragment ->
            isUserAuth = fragment.viewModel.authPreferences.isUserAuth()
        }

        if(isUserAuth){
            onView(withId(R.id.fragment_profile_layout)).check(matches(isDisplayed()))
            onView(withId(R.id.logOutBtn)).perform(click())
        }else{
            onView(withId(R.id.fragment_profile_guest_mode)).check(matches(isDisplayed()))
            onView(withId(R.id.toAuthBtn)).perform(click())
            onView(withId(R.id.activity_login_layout)).check(matches(isDisplayed()))
        }
    }
}