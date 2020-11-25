package com.stefanenko.coinbase.ui.fragment.favorites

import com.stefanenko.coinbase.domain.entity.ExchangeRate

sealed class StateFavorites {
    data class ShowErrorMessage(val error: String) : StateFavorites()
    data class ShowFavoritesRecycler(val itemList: List<ExchangeRate>) : StateFavorites()
}

sealed class InterruptibleState {
    object StartLoading : InterruptibleState()
    object StopLoading : InterruptibleState()
}