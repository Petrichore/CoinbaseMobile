package com.stefanenko.coinbase.ui.fragment.chart.chartFilter

import com.stefanenko.coinbase.domain.entity.ActiveCurrency
import com.stefanenko.coinbase.ui.fragment.chart.StateChart

sealed class StateFilter {
    data class ShowCurrencyRecycler(val itemList: List<ActiveCurrency>): StateFilter()
    object StartLoading : StateFilter()
    object StopLoading : StateFilter()
}

sealed class StateScattering {
    data class ShowErrorMessage(val error: String) : StateScattering()
    object ScatterLastState : StateScattering()
}

