package com.stefanenko.coinbase.ui.fragment.chart.chartFilter

import com.stefanenko.coinbase.domain.entity.ActiveCurrency

sealed class StateCurrencyFilter {
    object BlankStateCurrency: StateCurrencyFilter()
    object StartLoading : StateCurrencyFilter()
    object StopLoading : StateCurrencyFilter()
    data class ShowCurrencyRecycler(val itemList: List<ActiveCurrency>): StateCurrencyFilter()
    data class ShowErrorMessage(val error: String) : StateCurrencyFilter()
}

