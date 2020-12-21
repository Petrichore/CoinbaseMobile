package com.stefanenko.coinbase.ui.activity.appMain

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.ActivityMainBinding
import com.stefanenko.coinbase.ui.activity.splash.SplashActivity.Companion.CURRENCY_PARAM
import com.stefanenko.coinbase.ui.base.BaseActivity
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: SharedViewModel

    private lateinit var binding: ActivityMainBinding

    lateinit var toolbar: MaterialToolbar
    lateinit var menuBottomView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        menuBottomView = binding.menuBottom
        navController = findNavController(R.id.navHostFragmentMain)
        NavigationUI.setupWithNavController(menuBottomView, navController)
        setUpTopAppBar()
        getParams()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[SharedViewModel::class.java]
    }

    private fun getParams() {
        val extras = intent.extras
        if(extras != null){
            val param = extras.getString(CURRENCY_PARAM) ?: ""
            viewModel.setCurrency(param)
        }
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

}