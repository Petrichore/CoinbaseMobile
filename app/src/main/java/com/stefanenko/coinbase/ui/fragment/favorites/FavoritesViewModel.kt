package com.stefanenko.coinbase.ui.fragment.favorites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.repository.DataRepository
import com.stefanenko.coinbase.domain.useCase.FavoritesUseCases
import com.stefanenko.coinbase.util.espressoIdleResource.EspressoIdlingResource
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    private val favoritesUseCases: FavoritesUseCases,
    val authPreferences: AuthPreferences
) : ViewModel() {

    val state = MutableLiveData<StateFavorites>()

    private var deleteItemPosition: Int = -1
    private var deleteItem: ExchangeRate? = null

    fun getFavorites() {
        if (authPreferences.isUserAuth()) {
            state.value = StateFavorites.StartLoading
            EspressoIdlingResource.increment()
            viewModelScope.launch {
                val response = favoritesUseCases.getFavorites()
                when (response) {
                    is ResponseState.Data -> {
                        state.value =
                            StateFavorites.ShowFavoritesRecycler(response.data)
                    }
                    is ResponseState.Error -> {
                        state.value =
                            StateFavorites.ShowErrorMessage(response.error)
                    }
                }
                state.value = StateFavorites.StopLoading
                EspressoIdlingResource.decrement()
            }
        } else {
            state.value = StateFavorites.GuestMode
        }
    }

    fun itemDeleteIntent(itemPosition: Int, exchangeRate: ExchangeRate) {
        deleteItemPosition = itemPosition
        deleteItem = exchangeRate
        state.value = StateFavorites.ItemRemoveIntent(itemPosition)
        state.value = StateFavorites.ShowRetrieveItemSnackBar
    }

    fun cancelItemDelete() {
        if (deleteItemPosition != -1 && deleteItem != null) {
            state.value = StateFavorites.CancelItemDelete(deleteItemPosition, deleteItem!!)
            clearDeleteItemData()
        }
    }

    fun performDelete() {
        Log.d("Delete item::::", "$deleteItem")
        if (deleteItem != null) {
            deleteItem(deleteItem!!)
        }
    }

    private fun deleteItem(exchangeRate: ExchangeRate) {
        EspressoIdlingResource.increment()
        viewModelScope.launch {
            val response = favoritesUseCases.deleteFavorite(exchangeRate)
            when (response) {
                is ResponseState.Data -> state.value = StateFavorites.DeleteCompleted
                is ResponseState.Error -> state.value =
                    StateFavorites.ShowErrorMessage(response.error)
            }
            clearDeleteItemData()
            EspressoIdlingResource.decrement()
        }
    }

    private fun clearDeleteItemData() {
        deleteItemPosition = -1
        deleteItem = null
    }

    fun setBlankState() {
        state.value = StateFavorites.BlankState
    }
}