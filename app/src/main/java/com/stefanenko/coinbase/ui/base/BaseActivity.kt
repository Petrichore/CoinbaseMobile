package com.stefanenko.coinbase.ui.base

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.stefanenko.coinbase.R

abstract class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBarStyle()
        setContentView(getLayoutId())

        Log.e("onCreate", this.javaClass.name)
    }

    private fun setBarStyle(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            when(resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)){
                Configuration.UI_MODE_NIGHT_NO->{
                    Log.e("Change bar color", "yesss")
                    window.navigationBarColor = getColor(R.color.white)
                    window.statusBarColor = getColor(R.color.white)
                }
            }
        }
    }

    abstract fun getLayoutId(): Int
}