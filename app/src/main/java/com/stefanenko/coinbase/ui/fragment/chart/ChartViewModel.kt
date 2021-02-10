package com.stefanenko.coinbase.ui.fragment.chart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.stefanenko.coinbase.domain.entity.CurrencyMarketInfo
import com.stefanenko.coinbase.domain.entity.WebSocketState
import com.stefanenko.coinbase.domain.useCase.ChartUseCases
import com.stefanenko.coinbase.util.Mapper
import com.stefanenko.coinbase.util.exception.ERROR_INTERNET_CONNECTION
import com.stefanenko.coinbase.util.networkConnectivity.NetworkConnectivityManager
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ChartViewModel @Inject constructor(
    private val chartUseCases: ChartUseCases,
    private val connectivityManager: NetworkConnectivityManager,
    private val mapper: Mapper
) : ViewModel() {

    companion object {
        const val DEFAULT_CURRENCY = "XBTUSD"
        const val NOT_SPECIFIED = "NOT_SPECIFIED"
    }

    private lateinit var disposable: Disposable

    val state = MutableLiveData<StateChart>()

    private var itemCounter = 0.0f

    fun subscribeOnCurrencyDataFlow(currency: String) {
        if (connectivityManager.isConnected()) {
            state.value = StateChart.StartLoading
            disposable = chartUseCases.subscribeOnCurrencyDataFlow(currency) { wsState ->
                when (wsState) {
                    is WebSocketState.Data -> {
                        Log.d("WebSocketState:::", "DATA")
                        if (wsState.data.isNotEmpty()) {
                            val chartEntryList = mapper.map(wsState.data, itemCounter)
                            itemCounter += wsState.data.size
                            state.value = StateChart.OnNewMessage(chartEntryList)
                        }
                    }
                    is WebSocketState.Error -> {
                        state.value = StateChart.ShowErrorMessage(wsState.error)
                    }
                    is WebSocketState.Connected -> {
                        state.value = StateChart.OnConnectToWebSocket
                        state.value = StateChart.StopLoading
                    }
                }
            }
        } else {
            state.value = StateChart.ShowErrorMessage(ERROR_INTERNET_CONNECTION)
        }
    }

    fun unsubscribeFromCurrencyDataFlow() {
        if (::disposable.isInitialized) {
            chartUseCases.unsubscribeFromCurrencyDataFlow(disposable)
        }
    }

    fun setBlankState() {
        state.value = StateChart.BlankState
    }
}