package com.stefanenko.coinbase.ui.fragment.favorites

import com.stefanenko.coinbase.domain.entity.ExchangeRate

sealed class StateFavorites {
    object StartLoading : StateFavorites()
    object StopLoading : StateFavorites()
    object GuestMode: StateFavorites()
    data class ShowFavoritesRecycler(val itemList: List<ExchangeRate>) : StateFavorites()
    data class CancelItemDelete(val position: Int, val item: ExchangeRate) : StateFavorites()
    data class ItemRemoveIntent(val position: Int) : StateFavorites()

}

sealed class StateScattering {
    data class ShowErrorMessage(val error: String) : StateScattering()
    object ShowRetrieveItemSnackBar: StateScattering()
    object ScatterLastState : StateScattering()
}