package com.stefanenko.coinbase.ui.fragment.chart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.stefanenko.coinbase.domain.entity.CurrencyMarketInfo
import com.stefanenko.coinbase.domain.repository.WebSocketRepository
import com.stefanenko.coinbase.util.exception.ERROR_INTERNET_CONNECTION
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import javax.inject.Inject

class ChartViewModel @Inject constructor(
    private val webSocketRepository: WebSocketRepository,
    private val connectivityManager: NetworkConnectivityManager
) : ViewModel() {

    val state = MutableLiveData<StateChart>()
    val stateScattering = MutableLiveData<StateScattering>()

    private var itemCounter = 0

    fun getCurrencyData() {
        if(connectivityManager.isConnected()){
            state.value = StateChart.StartLoading
            webSocketRepository.subscribeOnCurrencyData {
                state.value = StateChart.StopLoading
                if (it.isNotEmpty()) {
                    state.value = StateChart.OnNewMessage(mapToEntryList(it))
                }
            }
        }else{
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

    fun stopWebSocket() {
        webSocketRepository.stopDataStream()
    }

    fun scatterStates(){
        stateScattering.value = StateScattering.ScatterLastState
    }
}