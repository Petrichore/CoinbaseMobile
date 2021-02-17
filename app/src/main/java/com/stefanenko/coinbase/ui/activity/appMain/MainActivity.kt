package com.stefanenko.coinbase.ui.activity.appMain

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.ActivityMainBinding
import com.stefanenko.coinbase.ui.base.BaseActivity
import dagger.android.AndroidInjection

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var toolbar: MaterialToolbar
    lateinit var menuBottomView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpTopAppBar()

        menuBottomView = binding.menuBottom
        navController = findNavController(R.id.navHostFragmentMain)
        NavigationUI.setupWithNavController(menuBottomView, navController)
        setOnDestinationChangedListener()
        showDebugLog("MainActivityCreated")
    }

    private fun setUpTopAppBar() {
        toolbar = binding.topAppBarMain.apply { menu.findItem(R.id.filter).isVisible = false }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.filter -> {
                    navController.navigate(R.id.action_chart_to_filterFragment)
                    true
                }
                else -> false
            }
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun setOnDestinationChangedListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            setUpActivityUI(destination)
        }
    }

    private fun setUpActivityUI(destination: NavDestination) {
        with(resources) {
            when (destination.label) {
                getString(R.string.nav_label_exchange_rates) -> {
                    toolbar.title = getString(R.string.toolbar_title_exchange_rate)
                }
                getString(R.string.nav_label_profile) -> {
                    toolbar.title = getString(R.string.toolbar_title_profile)
                }
                getString(R.string.nav_label_favorites) -> {
                    toolbar.title = getString(R.string.toolbar_title_favorites)
                }
                getString(R.string.nav_label_chart) -> {
                    toolbar.title = getString(R.string.toolbar_title_chart)
                }
                getString(R.string.nav_label_filter) -> {
                    menuBottomView.visibility = View.GONE
                    toolbar.title = getString(R.string.toolbar_title_filter)
                }
                getString(R.string.nav_label_settings) -> {
                    toolbar.title = getString(R.string.toolbar_title_settings)
                }
            }

            if (destination.label == getString(R.string.nav_label_chart) && !toolbar.menu.findItem(R.id.filter).isVisible) {
                toolbar.menu.findItem(R.id.filter).isVisible = true
            } else if (destination.label != getString(R.string.nav_label_chart) && toolbar.menu.findItem(R.id.filter).isVisible
            ) {
                toolbar.menu.findItem(R.id.filter).isVisible = false
            }
        }
    }
}