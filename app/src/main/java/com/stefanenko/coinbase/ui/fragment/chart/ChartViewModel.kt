package com.stefanenko.coinbase.ui.fragment.chart

import android.util.Log
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

    private var itemCounter = 0

    fun getCurrencyData() {
        webSocketRepository.getCurrencyRate {
            Log.d("currency data here", "YESSSSSS")
            if (it.isNotEmpty()) {
                state.value = StateChart.OnNewMessage(mapToEntryList(it))
            }
        }
    }

    private fun mapToEntryList(itemList: List<CurrencyMarketInfo>): List<Entry> {
        return itemList.map {
            itemCounter += 1
            Entry(itemCounter.toFloat(), it.price.toFloat())
        }
    }

    fun stopWebSocket() {
        webSocketRepository.stopDataStream()
    }
}