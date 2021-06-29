package com.stefanenko.coinbase.ui.fragment.chart

import com.github.mikephil.charting.data.Entry

sealed class StateChart {
    object BlankState: StateChart()
    object StartLoading : StateChart()
    object StopLoading : StateChart()
    object OnConnectToWebSocket : StateChart()
    data class OnNewMessage(val currencyEntryList: List<Entry>) : StateChart()
    data class ShowErrorMessage(val error: String) : StateChart()
}