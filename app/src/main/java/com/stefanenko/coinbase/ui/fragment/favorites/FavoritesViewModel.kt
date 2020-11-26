package com.stefanenko.coinbase.ui.fragment.favorites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.repository.DataRepository
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val authPreferences: AuthPreferences
) : ViewModel() {

    val state = MutableLiveData<StateFavorites>()
    val stateScattering = MutableLiveData<StateScattering>()

    private var deleteItemPosition: Int = -1
    private var deleteItem: ExchangeRate? = null

    fun getFavorites() {
        if(authPreferences.isUserAuth()){
            state.value = StateFavorites.StartLoading
            viewModelScope.launch {
                val response = dataRepository.getFavorites()
                when (response) {
                    is ResponseState.Data -> state.value =
                        StateFavorites.ShowFavoritesRecycler(response.data)
                    is ResponseState.Error -> stateScattering.value =
                        StateScattering.ShowErrorMessage(response.error)
                }
                state.value = StateFavorites.StopLoading
            }
        }else{
            state.value = StateFavorites.GuestMode
        }
    }

    fun itemDeleteIntent(itemPosition: Int, exchangeRate: ExchangeRate){
        deleteItemPosition = itemPosition
        deleteItem = exchangeRate
        state.value = StateFavorites.ItemRemoveIntent(itemPosition)
        stateScattering.value = StateScattering.ShowRetrieveItemSnackBar
    }

    fun cancelItemDelete(){
        if(deleteItemPosition != -1 && deleteItem != null){
            state.value = StateFavorites.CancelItemDelete(deleteItemPosition, deleteItem!!)
            clearDeleteItemData()
        }
    }

    fun performDelete(){
        Log.d("Delete item::::", "$deleteItem")
        if(deleteItem != null){
            deleteItem(deleteItem!!)
        }
    }

    private fun deleteItem(exchangeRate: ExchangeRate) {
        viewModelScope.launch {
            val response = dataRepository.deleteExchangeRateFromFavorites(exchangeRate)
            when (response) {
                is ResponseState.Error -> stateScattering.value =
                    StateScattering.ShowErrorMessage(response.error)
            }
            clearDeleteItemData()
        }
    }

    private fun clearDeleteItemData(){
        deleteItemPosition = -1
        deleteItem = null
    }

    fun scatterStates(){
        stateScattering.value = StateScattering.ScatterLastState
    }
}