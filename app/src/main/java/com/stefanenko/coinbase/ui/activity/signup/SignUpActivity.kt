package com.stefanenko.coinbase.ui.activity.signup

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.ActivitySignUpBinding
import com.stefanenko.coinbase.ui.base.BaseActivity

class SignUpActivity : BaseActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var navController: NavController
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.navHostFragmentSignUp)

        toolbar = binding.topAppBar as Toolbar
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}