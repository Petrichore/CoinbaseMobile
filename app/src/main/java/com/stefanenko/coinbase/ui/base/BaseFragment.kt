package com.stefanenko.coinbase.ui.base

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showDebugLog("OnViewCreated")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showDebugLog("onCreate")
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

    override fun onDestroyView() {
        super.onDestroyView()
        showDebugLog("OnDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        showDebugLog("OnDestroy")
    }

    protected fun showDebugLog(message: String) {
        Log.d(":::${this.javaClass.name}", message)
    }

    protected fun showAlertDialog(
        title: String,
        message: String,
        positiveAction: (dialog: DialogInterface) -> Unit,
        negativeAction: (dialog: DialogInterface) -> Unit
    ) {
        AlertDialog.Builder(context)
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
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    fun startActivityInNewTask(activityClass: Class<out Activity>) {
        val intent = Intent(requireContext(), activityClass).apply {
            flags.and(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
        finishAffinity(requireActivity())
    }
}