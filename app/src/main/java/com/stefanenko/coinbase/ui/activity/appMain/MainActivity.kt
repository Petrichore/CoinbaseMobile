package com.stefanenko.coinbase.ui.activity.appMain

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    lateinit var toolbar: MaterialToolbar
    lateinit var menuBottomView: BottomNavigationView
    private lateinit var navController: NavController

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuBottomView = menuBottom
        navController = findNavController(R.id.navHostFragmentMain)
        NavigationUI.setupWithNavController(menuBottomView, navController)
        setUpTopAppBar()
    }

    private fun setUpTopAppBar() {
        toolbar = topAppBarMain.apply { menu.findItem(R.id.filter).isVisible = false }
        toolbar.setOnMenuItemClickListener { menuItem->
            when(menuItem.itemId){
                R.id.filter->{
                    navController.navigate(R.id.action_chart_to_filterFragment)
                    true
                }
                else-> false
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