package com.stefanenko.coinbase.ui.fragment.exchangeRate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.exception.ERROR_UNAUTHORIZED
import com.stefanenko.coinbase.domain.repository.DataRepository
import com.stefanenko.coinbase.util.exception.ERROR_INTERNET_CONNECTION
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExchangeRatesViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val authPreferences: AuthPreferences,
    private val connectivityManager: NetworkConnectivityManager
) : ViewModel() {

    val state = MutableLiveData<StateExchangeRates>()
    val interruptibleState = MutableLiveData<InterruptibleState>()

    fun getExchangeRates(currency: String) {
        if (connectivityManager.isConnected()) {
            interruptibleState.value = InterruptibleState.StartLoading

            viewModelScope.launch {
                val responseState = dataRepository.getProfile(
                    authPreferences.getAccessToken(),
                    authPreferences.getRefreshToken(),
                    currency
                )
                when (responseState) {
                    is ResponseState.Data -> state.value =
                        StateExchangeRates.ShowExchangeRateRecycler(responseState.data)
                    is ResponseState.Error -> {
                        state.value = StateExchangeRates.ShowErrorMessage(responseState.error)
                    }
                }

                interruptibleState.value = InterruptibleState.StopLoading
            }

        } else {
            state.value = StateExchangeRates.ShowErrorMessage(ERROR_INTERNET_CONNECTION)
        }
    }
}