package com.stefanenko.coinbase.ui.activity.appMain

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SharedViewModel @Inject constructor() : ViewModel() {

    private var deepLinkCurrency: String = ""

    fun setCurrency(currency: String) {
        if (currency.isNotEmpty()) {
            deepLinkCurrency = currency
        } else {
            deepLinkCurrency = ""
        }
    }

    fun getCurrency(): String {
        return deepLinkCurrency
    }
}