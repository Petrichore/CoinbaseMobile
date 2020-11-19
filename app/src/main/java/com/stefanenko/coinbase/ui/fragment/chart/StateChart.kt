package com.stefanenko.coinbase.ui.fragment.chart

import com.github.mikephil.charting.data.Entry
import com.stefanenko.coinbase.domain.entity.CurrencyMarketInfo

sealed class StateChart {
    data class ShowErrorMessage(val error: String) : StateChart()
    data class OnNewMessage(val currencyEntryList: List<Entry>) : StateChart()
    object OnConnectToWebSocket : StateChart()
    object OnDisconnectFromWebSocket : StateChart()
}

sealed class InterruptibleStateChart {
    object StartLoading : InterruptibleStateChart()
    object StopLoading : InterruptibleStateChart()
}