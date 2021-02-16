package com.stefanenko.coinbase.ui.base

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.stefanenko.coinbase.R

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBarStyle()
        overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
        Log.e("onCreate", this.javaClass.name)
    }

    private fun setBarStyle() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    Log.e("Change bar color", "yesss")
                    window.navigationBarColor = getColor(R.color.white)
                    window.statusBarColor = getColor(R.color.white)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showDebugLog("onResume")
    }

    override fun onPause() {
        super.onPause()
        showDebugLog("OnPause")
    }

    override fun onStop() {
        super.onStop()
        showDebugLog("OnStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        showDebugLog("OnDestroy")
    }

    fun showDebugLog(message: String) {
        Log.d(":::${this.javaClass.name}", message)
    }

    fun startActivityInNewTask(activityClass: Class<out Activity>, paramKeyValue: Pair<String, String>) {
        val intent = Intent(this, activityClass).apply {
            flags.and(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra(paramKeyValue.first, paramKeyValue.second)
        }
        startActivity(intent)
        finishAffinity()
    }

    fun startActivityInNewTask(activityClass: Class<out Activity>) {
        val intent = Intent(this, activityClass).apply {
            flags.and(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
        finishAffinity()
    }

    fun startActivity(activityClass: Class<out Activity>, finishCurrent: Boolean) {
        val intent = Intent(this, activityClass)
        startActivity(intent)

        if (finishCurrent) finish()
    }

    protected fun showAlertDialog(
        title: String,
        message: String,
        positiveAction: (dialog: DialogInterface) -> Unit,
        negativeAction: (dialog: DialogInterface) -> Unit
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok") { dialog, _ ->
                positiveAction.invoke(dialog)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                negativeAction.invoke(dialog)
            }
            .create()
            .show()
    }

    protected fun showInfoDialog(
        title: String,
        message: String,
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}