package com.stefanenko.coinbase.ui.fragment.exchangeRate

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.useCase.ExchangeRateUseCases
import com.stefanenko.coinbase.domain.useCase.FavoritesUseCases
import com.stefanenko.coinbase.util.exception.ERROR_INTERNET_CONNECTION
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import com.stefanenko.coinbase.util.preferences.AuthPreferences
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExchangeRatesViewModel @Inject constructor(
    private val exchangeUseCases: ExchangeRateUseCases,
    private val favoritesUseCases: FavoritesUseCases,
    private val authPreferences: AuthPreferences,
    private val connectivityManager: NetworkConnectivityManager
) : ViewModel() {

    companion object {
        const val DEFAULT_BASE_CURRENCY = "USD"
    }

    val state: MutableLiveData<StateExchangeRates> = MutableLiveData<StateExchangeRates>()
    val stateScattering: MutableLiveData<StateScattering> = MutableLiveData<StateScattering>()

    init {
        if (!connectivityManager.isConnected()) {
            state.value = StateExchangeRates.NetworkUnavailable
        }
    }

    private var networkCallback = connectivityManager.regNetworkCallBack({
        state.postValue(StateExchangeRates.NetworkAvailable)
    }, {
        stateScattering.postValue(StateScattering.ShowErrorMessage(ERROR_INTERNET_CONNECTION))
    })

    fun getExchangeRates(baseCurrency: String) {
        Log.d("GET EXCHANGE RATE CALLED", "Yeeees")
        if (connectivityManager.isConnected()) {
            getExchangeRatesRemote(baseCurrency)
        } else {
            getCashedExchangeRates()
        }
    }

    fun getExchangeRatesRemote(baseCurrency: String) {
        Log.d("PERFORM GET EXCHANGE RATE CALLED", "Yeeees")
        stateScattering.value = StateScattering.StartLoading
        viewModelScope.launch {
            when (val responseState = exchangeUseCases.getExchangeRates(baseCurrency)) {
                is ResponseState.Data -> state.value =
                    StateExchangeRates.ShowExchangeRateRecycler(responseState.data)

                is ResponseState.Error -> {
                    stateScattering.value = StateScattering.ShowErrorMessage(responseState.error)
                }
            }
            stateScattering.value = StateScattering.StopLoading
        }
    }

    fun getCashedExchangeRates() {
        Log.d("CASHED GET EXCHANGE RATE CALLED", "Yeeees")

        viewModelScope.launch {
            when (val responseState = exchangeUseCases.getCashedExchangeRates()) {
                is ResponseState.Data -> state.value =
                    StateExchangeRates.ShowExchangeRateRecycler(responseState.data)
                is ResponseState.Error -> {
                    stateScattering.value =
                        StateScattering.ShowErrorMessage(responseState.error)
                }
            }
            stateScattering.value = StateScattering.StopLoading
        }
    }

    fun updateExchangeRates(baseCurrency: String) {
        Log.d("UPDATE EXCHANGE RATE CALLED", "Yeeees")

        if (connectivityManager.isConnected()) {
            viewModelScope.launch {
                when (val responseState = exchangeUseCases.updateExchangeRates(baseCurrency)) {
                    is ResponseState.Data -> state.value =
                        StateExchangeRates.UpdateExchangeRateRecycler(responseState.data)
                    is ResponseState.Error -> {
                        stateScattering.value =
                            StateScattering.ShowErrorMessage(responseState.error)
                    }
                }
                stateScattering.value = StateScattering.StopLoading
            }
        } else {
            stateScattering.value = StateScattering.ShowErrorMessage(ERROR_INTERNET_CONNECTION)
        }
    }

    fun addCurrencyToFavorite(exchangeRate: ExchangeRate) {
        viewModelScope.launch {
            val response = favoritesUseCases.addFavorite(exchangeRate)
            when (response) {
                is ResponseState.Data -> {
                    stateScattering.value = StateScattering.ShowSnackBar
                    stateScattering.value = StateScattering.ScatterLastState
                }
                is ResponseState.Error -> {

                }
            }
        }
    }

    fun checkAbilityToSaveCurrency(exchangeRate: ExchangeRate) {
        if (authPreferences.isUserAuth()) {
            stateScattering.value = StateScattering.ShowDialogSaveToFav(exchangeRate)
        } else {
            stateScattering.value = StateScattering.ShowDialogUserAuthMissing
        }
    }


    fun scatterStates() {
        stateScattering.value = StateScattering.ScatterLastState
    }

    override fun onCleared() {
        super.onCleared()
        connectivityManager.revokeNetworkCallBack(networkCallback)
    }
}