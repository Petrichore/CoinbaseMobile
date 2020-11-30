package com.stefanenko.coinbase.ui.fragment.chart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.stefanenko.coinbase.domain.entity.CurrencyMarketInfo
import com.stefanenko.coinbase.domain.useCase.RealTimeChartUseCases
import com.stefanenko.coinbase.util.exception.ERROR_INTERNET_CONNECTION
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import javax.inject.Inject

class ChartViewModel @Inject constructor(
    private val chartUseCases: RealTimeChartUseCases,
    private val connectivityManager: NetworkConnectivityManager
) : ViewModel() {

    companion object {
        const val MISSING_SELECTED_CURRENCY = "MISSING_SELECTED_CURRENCY"
    }

    val state = MutableLiveData<StateChart>()
    val stateScattering = MutableLiveData<StateScattering>()

    private val defaultCurrency = "XBTUSD"

    private var itemCounter = 0

    fun subscribeOnCurrencyDataFlow(currency: String) {
        if (connectivityManager.isConnected()) {
            state.value = StateChart.StartLoading
            val selectedCurrency = mapSelectedCurrency(currency)
            Log.d("SELECTED CURRENCY", selectedCurrency)
            chartUseCases.subscribeOnCurrencyDataFlow(selectedCurrency) {
                state.value = StateChart.StopLoading
                if (it.isNotEmpty()) {
                    state.value = StateChart.OnNewMessage(mapToEntryList(it))
                }
            }
        } else {
            stateScattering.value = StateScattering.ShowErrorMessage(ERROR_INTERNET_CONNECTION)
        }
    }

    private fun mapToEntryList(itemList: List<CurrencyMarketInfo>): List<Entry> {
        val entryList = mutableListOf<Entry>()

        for (i in itemList.indices) {
            entryList.add(Entry(itemCounter++.toFloat(), itemList[i].price))
        }

        return entryList
    }

    private fun mapSelectedCurrency(selectedCurrency: String): String {
        if (selectedCurrency == MISSING_SELECTED_CURRENCY) {
            return defaultCurrency
        } else {
            return selectedCurrency
        }
    }

    fun unsubscribeFromCurrencyDataFlow() {
        chartUseCases.unsubscribeFromCurrencyDataFlow()
    }

    fun scatterStates() {
        stateScattering.value = StateScattering.ScatterLastState
    }
}