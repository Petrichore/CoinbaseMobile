package com.stefanenko.coinbase.ui.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import dagger.android.support.AndroidSupportInjection

abstract class BaseObserveFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    protected abstract fun initViewModel()
    protected abstract fun initObservers()
}