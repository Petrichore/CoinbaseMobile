package com.stefanenko.coinbase.profile

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.domain.entity.Profile
import com.stefanenko.coinbase.ui.fragment.profile.ProfileFragment
import com.stefanenko.coinbase.ui.fragment.profile.StateProfile
import okio.Timeout
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class ProfileFragmentTest {

//    @get:Rule
//    var mainActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun profile_guest_mode() {
        val fragmentScenario =
            launchFragmentInContainer<ProfileFragment>(null, R.style.Theme_CoinbaseMobile)
        fragmentScenario.onFragment { fragment ->
            fragment.viewModel.state.value = StateProfile.GuestMode
        }

        onView(withId(R.id.fragment_profile_guest_mode)).check(matches(isDisplayed()))
        onView(withId(R.id.toAuthBtn)).perform(click())
        onView(withId(R.id.activity_login_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun profile_info() {
        val fragmentScenario =
            launchFragmentInContainer<ProfileFragment>(null, R.style.Theme_CoinbaseMobile)

        val profile = Profile("Ivan", "Ctefivan123", "https//:", "Belarus")
        fragmentScenario.onFragment { fragment ->
            fragment.viewModel.state.value = StateProfile.ShowProfileData(profile)
        }

        onView(withId(R.id.fragment_profile_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.logOutBtn)).perform(click())
        Thread.sleep(1000)
//        onView(withId(R.id.activity_login_layout)).check(matches(isDisplayed()))
    }
}