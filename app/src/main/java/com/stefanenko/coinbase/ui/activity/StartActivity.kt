package com.stefanenko.coinbase.ui.activity

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.base.BaseActivity

class StartActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_start

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController(R.id.navHostFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}