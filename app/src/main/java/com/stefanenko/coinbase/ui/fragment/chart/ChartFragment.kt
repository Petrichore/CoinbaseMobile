package com.stefanenko.coinbase.ui.fragment.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.FragmentChartBinding
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.activity.appMain.SharedViewModel
import com.stefanenko.coinbase.ui.base.BaseObserveFragment
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import com.stefanenko.coinbase.ui.fragment.chart.ChartViewModel.Companion.DEFAULT_CURRENCY
import com.stefanenko.coinbase.ui.fragment.chart.chartFilter.FilterFragment
import com.stefanenko.coinbase.util.getNavigationResult
import kotlinx.android.synthetic.main.fragment_chart.*
import javax.inject.Inject

class ChartFragment : BaseObserveFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ChartViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentChartBinding? = null
    private val binding: FragmentChartBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currencyParam = ChartFragmentArgs.fromBundle(requireArguments()).currency
        val selectedCurrency: String

        if (currencyParam != NOT_SPECIFIED) {
            selectedCurrency = currencyParam
            showDebugLog("DEEP LINK PARAM:::: $currencyParam")
        } else {
            selectedCurrency = DEFAULT_CURRENCY
        }

        (activity as MainActivity).toolbar.title = "${resources.getString(R.string.title_chart)} ($selectedCurrency)"

        configChart(selectedCurrency)
        viewModel.subscribeOnCurrencyDataFlow(selectedCurrency)
    }

    private fun configChart(selectedCurrency: String) {

        val chartConfig = LineDataSet(mutableListOf(), selectedCurrency).apply {
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
                is StateChart.OnNewMessage -> {
                    updateChart(state.currencyEntryList)
                }
                StateChart.StartLoading -> {
                    chart.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                StateChart.StopLoading -> {
                    chart.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        })

        viewModel.stateScattering.observe(viewLifecycleOwner, { scatteringState ->
            when (scatteringState) {
                is StateScattering.ShowErrorMessage -> {
                    showInfoDialog("Websocket Error", scatteringState.error)
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
        viewModel.unsubscribeFromCurrencyDataFlow()
        viewModel.scatterStates()
        (activity as MainActivity).toolbar.menu.findItem(R.id.filter).isVisible = false
    }
}