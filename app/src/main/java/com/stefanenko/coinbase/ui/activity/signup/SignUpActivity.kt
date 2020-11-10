package com.stefanenko.coinbase.ui.activity.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_sign_up

    private lateinit var navController: NavController
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController(R.id.navHostFragmentSignUp)

        toolbar = topAppBar as Toolbar
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}