package com.stefanenko.coinbase.ui.fragment.exchangeRate

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.useCase.ExchangeRateUseCases
import com.stefanenko.coinbase.domain.useCase.FavoritesUseCases
import com.stefanenko.coinbase.util.espressoIdleResource.EspressoIdlingResource
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

//    init {
//        if (!connectivityManager.isConnected()) {
//            state.value = StateExchangeRates.NetworkUnavailable
//        }
//    }

    private var networkCallback = connectivityManager.regNetworkCallBack({
        state.postValue(StateExchangeRates.NetworkAvailable)
    }, {
        state.postValue(StateExchangeRates.ShowErrorMessage(ERROR_INTERNET_CONNECTION))
    })

    fun getExchangeRates(baseCurrency: String) {
        Log.d("GET EXCHANGE RATE CALLED", "Yeeees")
        if (connectivityManager.isConnected()) {
            getExchangeRatesRemote(baseCurrency)
        } else {
            getCashedExchangeRates()
        }
    }

    private fun getExchangeRatesRemote(baseCurrency: String) {
        Log.d("PERFORM GET EXCHANGE RATE CALLED", "Yeeees")
        state.value = StateExchangeRates.StartLoading
        EspressoIdlingResource.increment()
        viewModelScope.launch {
            when (val responseState = exchangeUseCases.getExchangeRates(baseCurrency)) {
                is ResponseState.Data -> {
                    state.value =
                        StateExchangeRates.ShowExchangeRateRecycler(responseState.data)
                }

                is ResponseState.Error -> {
                    state.value = StateExchangeRates.ShowErrorMessage(responseState.error)
                }
                else -> Unit
            }
            state.value = StateExchangeRates.StopLoading
            EspressoIdlingResource.decrement()
        }
    }

    fun getCashedExchangeRates() {
        Log.d("CASHED GET EXCHANGE RATE CALLED", "Yeeees")
        state.value = StateExchangeRates.StartLoading
        EspressoIdlingResource.increment()
        viewModelScope.launch {
            when (val responseState = exchangeUseCases.getCashedExchangeRates()) {
                is ResponseState.Data -> {
                    state.value =
                        StateExchangeRates.ShowExchangeRateRecycler(responseState.data)
                }
                is ResponseState.Error -> {
                    state.value =
                        StateExchangeRates.ShowErrorMessage(responseState.error)
                }
                else -> Unit
            }
            state.value = StateExchangeRates.StopLoading
            EspressoIdlingResource.decrement()
        }
    }

    fun updateExchangeRates(baseCurrency: String) {
        Log.d("UPDATE EXCHANGE RATE CALLED", "Yeeees")
        if (connectivityManager.isConnected()) {
            state.value = StateExchangeRates.StartLoading
            EspressoIdlingResource.increment()

            viewModelScope.launch {
                when (val responseState = exchangeUseCases.updateExchangeRates(baseCurrency)) {
                    is ResponseState.Data -> {
                        state.value =
                            StateExchangeRates.UpdateExchangeRateRecycler(responseState.data)
                    }
                    is ResponseState.Error -> {
                        state.value =
                            StateExchangeRates.ShowErrorMessage(responseState.error)
                    }
                    else -> Unit
                }
                state.value = StateExchangeRates.StopLoading
                EspressoIdlingResource.decrement()
            }
        } else {
            state.value = StateExchangeRates.ShowErrorMessage(ERROR_INTERNET_CONNECTION)
            state.value = StateExchangeRates.StopLoading
        }
    }

    fun addCurrencyToFavorite(exchangeRate: ExchangeRate) {
        EspressoIdlingResource.increment()
        viewModelScope.launch {
            val response = favoritesUseCases.addFavorite(exchangeRate)
            when (response) {
                is ResponseState.Data -> {
                    state.value = StateExchangeRates.ShowSnackBar
                }
                is ResponseState.Error -> {
                    state.value =
                        StateExchangeRates.ShowErrorMessage(response.error)
                }
                else -> Unit
            }
            EspressoIdlingResource.decrement()
        }
    }

    fun checkAbilityToSaveCurrency(exchangeRate: ExchangeRate) {
        if (authPreferences.isUserAuth()) {
            state.value = StateExchangeRates.ShowDialogSaveToFav(exchangeRate)
        } else {
            state.value = StateExchangeRates.ShowDialogUserAuthMissing
        }
    }


    fun setBlankState() {
        state.value = StateExchangeRates.BlankState
    }

    override fun onCleared() {
        super.onCleared()
        connectivityManager.revokeNetworkCallBack(networkCallback)
    }
}