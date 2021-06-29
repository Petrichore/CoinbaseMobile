package com.stefanenko.coinbase.fragment.chart

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.fragment.chart.ChartFragment
import com.stefanenko.coinbase.util.espressoIdleResource.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ChartFragmentTest {

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun reset() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun chart_visible_when_connection_established() {

        val testNavController = TestNavHostController(ApplicationProvider.getApplicationContext())
        testNavController.setGraph(R.navigation.nav_graph_main)

        val bundle = Bundle()
        launchFragmentInContainer {
            ChartFragment().apply {
                viewLifecycleOwnerLiveData.observeForever { lifecycleOwner ->
                    if (lifecycleOwner != null) {
                        arguments = bundle
                        Navigation.setViewNavController(requireView(), testNavController)
                    }
                }
            }
        }

        onView(withId(R.id.chartView)).check(matches(isDisplayed()))
    }
}