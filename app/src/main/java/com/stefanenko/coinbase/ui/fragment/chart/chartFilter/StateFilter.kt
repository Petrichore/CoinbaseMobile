package com.stefanenko.coinbase.ui.fragment.chart.chartFilter

import com.stefanenko.coinbase.domain.entity.ActiveCurrency
import com.stefanenko.coinbase.ui.fragment.chart.StateChart

sealed class StateFilter {
    object BlankState: StateFilter()
    object StartLoading : StateFilter()
    object StopLoading : StateFilter()
    data class ShowCurrencyRecycler(val itemList: List<ActiveCurrency>): StateFilter()
    data class ShowErrorMessage(val error: String) : StateFilter()
}

