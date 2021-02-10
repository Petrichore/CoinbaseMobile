package com.stefanenko.coinbase.ui.fragment.favorites

import com.stefanenko.coinbase.domain.entity.ExchangeRate

sealed class StateFavorites {
    object BlankState: StateFavorites()
    object StartLoading : StateFavorites()
    object StopLoading : StateFavorites()
    object GuestMode: StateFavorites()
    object DeleteCompleted: StateFavorites()
    data class ShowErrorMessage(val error: String) : StateFavorites()
    object ShowRetrieveItemSnackBar: StateFavorites()
    data class ShowFavoritesRecycler(val itemList: List<ExchangeRate>) : StateFavorites()
    data class CancelItemDelete(val position: Int, val item: ExchangeRate) : StateFavorites()
    data class ItemRemoveIntent(val position: Int) : StateFavorites()
}