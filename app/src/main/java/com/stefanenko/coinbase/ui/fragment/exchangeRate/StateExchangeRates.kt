package com.stefanenko.coinbase.ui.fragment.exchangeRate

import com.stefanenko.coinbase.domain.entity.ExchangeRate

sealed class StateExchangeRates {
    data class ShowErrorMessage(val error: String) : StateExchangeRates()
    data class ShowExchangeRateRecycler(val itemList: List<ExchangeRate>) : StateExchangeRates()
    data class UpdateExchangeRateRecycler(
        val nItemList: List<ExchangeRate>,
        val oItemList: List<ExchangeRate>
    ) : StateExchangeRates()
}

sealed class InterruptibleState {
    object StartLoading : InterruptibleState()
    object StopLoading : InterruptibleState()
}