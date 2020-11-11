package com.stefanenko.coinbase.ui.fragment.exchangeRate

import android.os.Bundle
import android.view.View
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.base.BaseFragment

class ExchangeRateFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_exchange_rate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).toolbar.title =
            resources.getString(R.string.toolbar_title_exchange_rate)
    }
}