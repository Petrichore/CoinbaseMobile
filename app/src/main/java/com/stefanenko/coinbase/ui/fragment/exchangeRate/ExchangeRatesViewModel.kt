package com.stefanenko.coinbase.ui.fragment.exchangeRate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.ResponseState
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
    val scatteringState = MutableLiveData<StateScattering>()

    fun getExchangeRates(baseCurrency: String) {
        if (connectivityManager.isConnected()) {
            state.value = StateExchangeRates.StartLoading

            viewModelScope.launch {
                when (val responseState = dataRepository.getCurrenciesRates(baseCurrency)) {
                    is ResponseState.Data -> state.value =
                        StateExchangeRates.ShowExchangeRateRecycler(responseState.data)
                    is ResponseState.Error -> {
                        scatteringState.value =
                            StateScattering.ShowErrorMessage(responseState.error)
                    }
                }

                state.value = StateExchangeRates.StopLoading
            }

        } else {
            scatteringState.value = StateScattering.ShowErrorMessage(ERROR_INTERNET_CONNECTION)
        }
    }

    fun addCurrencyToFavorite(exchangeRate: ExchangeRate) {
        viewModelScope.launch {
            val response = dataRepository.addCurrencyToFavorite(exchangeRate)
            when (response) {
                is ResponseState.Data -> {
                    scatteringState.value = StateScattering.ShowSnackBar
                    scatteringState.value = StateScattering.ScatterLastState
                }
                is ResponseState.Error -> {

                }
            }
        }
    }

    fun checkAbilityToSaveCurrency(exchangeRate: ExchangeRate) {
        if (authPreferences.isUserAuth()) {
            scatteringState.value = StateScattering.ShowDialogSaveToFav(exchangeRate)
        } else {
            scatteringState.value = StateScattering.ShowDialogUserAuthMissing
        }
    }
}