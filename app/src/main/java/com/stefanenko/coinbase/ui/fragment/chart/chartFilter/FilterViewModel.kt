package com.stefanenko.coinbase.ui.fragment.chart.chartFilter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.useCase.ChartFilterUseCases
import com.stefanenko.coinbase.util.exception.ERROR_INTERNET_CONNECTION
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilterViewModel @Inject constructor(
    private val chartFilterUseCases: ChartFilterUseCases,
    private val connectivityManager: NetworkConnectivityManager
) : ViewModel() {

    val state = MutableLiveData<StateFilter>()

    fun getActiveCurrency() {
        if (connectivityManager.isConnected()) {
            state.value = StateFilter.StartLoading
            viewModelScope.launch {
                val response = chartFilterUseCases.getActiveCurrency()

                when (response) {
                    is ResponseState.Data -> state.value =
                        StateFilter.ShowCurrencyRecycler(response.data)

                    is ResponseState.Error -> state.value =
                        StateFilter.ShowErrorMessage(response.error)
                }

                state.value = StateFilter.StopLoading
            }
        } else {
            state.value = StateFilter.ShowErrorMessage(ERROR_INTERNET_CONNECTION)
        }
    }
}