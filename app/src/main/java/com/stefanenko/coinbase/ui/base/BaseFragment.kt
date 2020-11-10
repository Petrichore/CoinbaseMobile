package com.stefanenko.coinbase.ui.base

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment : Fragment() {

    protected abstract fun getLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showDebugLog("onCreateView")
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        showDebugLog("OnViewCreated")
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
}