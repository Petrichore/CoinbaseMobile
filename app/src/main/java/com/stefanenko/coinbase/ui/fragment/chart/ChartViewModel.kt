package com.stefanenko.coinbase.ui.fragment.chart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.stefanenko.coinbase.domain.entity.CurrencyMarketInfo
import com.stefanenko.coinbase.domain.repository.WebSocketRepository
import javax.inject.Inject

class ChartViewModel @Inject constructor(
    private val webSocketRepository: WebSocketRepository
) : ViewModel() {

    val state = MutableLiveData<StateChart>()
    val interruptibleStateChart = MutableLiveData<InterruptibleStateChart>()

    private var itemCounter = 0

    fun getCurrencyData() {
        interruptibleStateChart.value = InterruptibleStateChart.StartLoading
        webSocketRepository.subscribeOnCurrencyData {
            interruptibleStateChart.value = InterruptibleStateChart.StopLoading
            if (it.isNotEmpty()) {
                state.value = StateChart.OnNewMessage(mapToEntryList(it))
            }
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
}