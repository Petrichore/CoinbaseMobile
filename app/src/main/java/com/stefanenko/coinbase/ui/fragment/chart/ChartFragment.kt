package com.stefanenko.coinbase.ui.fragment.chart

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.base.BaseObserveFragment
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_chart.*
import javax.inject.Inject

class ChartFragment : BaseObserveFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ChartViewModel

    override fun getLayoutId(): Int = R.layout.fragment_chart

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).toolbar.title = resources.getString(R.string.title_chart)
        configChart()
        viewModel.getCurrencyData()
    }

    private fun configChart() {
        val chartConfig = LineDataSet(mutableListOf(), "XBTUSD").apply {
            lineWidth = 3f
            setDrawValues(false)
            setDrawCircles(false)
            color = resources.getColor(R.color.main_green)
        }

        chart.data = LineData(chartConfig)

        with(chart) {
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.isGranularityEnabled = false
            axisRight.isEnabled = false
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[ChartViewModel::class.java]
    }

    override fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is StateChart.ShowErrorMessage -> {
                    showInfoDialog("Websocket Error", state.error)
                }
                is StateChart.OnNewMessage -> {
                    updateChart(state.currencyEntryList)
                }
                StateChart.OnConnectToWebSocket -> {
                }
                StateChart.OnDisconnectFromWebSocket -> {
                }
            }
        })

        viewModel.interruptibleStateChart.observe(viewLifecycleOwner, { interruptibleState ->
            when (interruptibleState) {
                InterruptibleStateChart.StartLoading -> {
                    chart.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                InterruptibleStateChart.StopLoading -> {
                    chart.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun updateChart(itemList: List<Entry>) {
        val data = chart.data

        for (i in itemList.indices) {
            data.getDataSetByIndex(0).addEntry(itemList[i])
        }

        data.notifyDataChanged()
        chart.notifyDataSetChanged()

        chart.moveViewTo(
            (data.entryCount).toFloat(),
            50f,
            YAxis.AxisDependency.LEFT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stopWebSocket()
    }
}