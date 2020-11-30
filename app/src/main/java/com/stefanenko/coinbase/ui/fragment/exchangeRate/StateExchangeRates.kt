package com.stefanenko.coinbase.ui.fragment.exchangeRate

import com.stefanenko.coinbase.domain.entity.ExchangeRate

sealed class StateExchangeRates {
    object StartLoading : StateExchangeRates()
    object StopLoading : StateExchangeRates()
    data class ShowExchangeRateRecycler(val itemList: List<ExchangeRate>) : StateExchangeRates()
    data class UpdateExchangeRateRecycler(
        val nItemList: List<ExchangeRate>
    ) : StateExchangeRates()
}

sealed class StateScattering {
    object ShowSnackBar : StateScattering()
    object ScatterLastState : StateScattering()
    object ShowDialogUserAuthMissing : StateScattering()
    data class ShowErrorMessage(val error: String) : StateScattering()
    data class ShowDialogSaveToFav(val exchangeRate: ExchangeRate) : StateScattering()
}