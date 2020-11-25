package com.stefanenko.coinbase.ui.fragment.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.repository.DataRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    val state = MutableLiveData<StateFavorites>()
    val interruptibleState = MutableLiveData<InterruptibleState>()

    fun getFavorites() {
        interruptibleState.value = InterruptibleState.StartLoading
        viewModelScope.launch {
            val response = dataRepository.getFavorites()
            when (response) {
                is ResponseState.Data -> state.value =
                    StateFavorites.ShowFavoritesRecycler(response.data)
                is ResponseState.Error -> state.value =
                    StateFavorites.ShowErrorMessage(response.error)
            }
            interruptibleState.value = InterruptibleState.StopLoading
        }
    }
}