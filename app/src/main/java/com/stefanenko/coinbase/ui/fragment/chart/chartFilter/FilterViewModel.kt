package com.stefanenko.coinbase.ui.fragment.chart.chartFilter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefanenko.coinbase.domain.entity.ResponseState
import com.stefanenko.coinbase.domain.useCase.ChartFilterUseCases
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilterViewModel @Inject constructor(
    private val chartFilterUseCases: ChartFilterUseCases
) : ViewModel() {

    val state = MutableLiveData<StateFilter>()
    val stateScattering = MutableLiveData<StateScattering>()

    fun getActiveCurrency() {
        state.value = StateFilter.StartLoading
        viewModelScope.launch {
            val response = chartFilterUseCases.getActiveCurrency()

            when (response) {
                is ResponseState.Data -> state.value =
                    StateFilter.ShowCurrencyRecycler(response.data)

                is ResponseState.Error -> stateScattering.value =
                    StateScattering.ShowErrorMessage(response.error)
            }

            state.value = StateFilter.StopLoading
        }
    }
}