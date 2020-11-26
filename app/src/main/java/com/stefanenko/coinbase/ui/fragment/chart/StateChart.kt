package com.stefanenko.coinbase.ui.fragment.chart

import com.github.mikephil.charting.data.Entry

sealed class StateChart {
    object StartLoading : StateChart()
    object StopLoading : StateChart()
    data class OnNewMessage(val currencyEntryList: List<Entry>) : StateChart()
    object OnConnectToWebSocket : StateChart()
    object OnDisconnectFromWebSocket : StateChart()
}

sealed class StateScattering {
    data class ShowErrorMessage(val error: String) : StateScattering()
    object ScatterLastState : StateScattering()
}