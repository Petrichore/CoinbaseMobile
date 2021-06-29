package com.stefanenko.coinbase.ui.fragment.exchangeRate

import com.stefanenko.coinbase.domain.entity.ExchangeRate

sealed class StateExchangeRates {
    object BlankState : StateExchangeRates()
    object NetworkAvailable : StateExchangeRates()
    object NetworkUnavailable : StateExchangeRates()
    object StartLoading : StateExchangeRates()
    object StopLoading : StateExchangeRates()
    object ShowSnackBar : StateExchangeRates()
    object ShowDialogUserAuthMissing : StateExchangeRates()
    data class ShowErrorMessage(val error: String) : StateExchangeRates()
    data class ShowDialogSaveToFav(val exchangeRate: ExchangeRate) : StateExchangeRates()
    data class ShowExchangeRateRecycler(val itemList: List<ExchangeRate>) : StateExchangeRates()
    data class UpdateExchangeRateRecycler(
        val nItemList: List<ExchangeRate>
    ) : StateExchangeRates()
}