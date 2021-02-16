package com.stefanenko.coinbase.ui.fragment.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.FragmentChartBinding
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.base.BaseObserveFragment
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import com.stefanenko.coinbase.ui.fragment.chart.ChartViewModel.Companion.DEFAULT_CURRENCY
import com.stefanenko.coinbase.ui.fragment.chart.ChartViewModel.Companion.NOT_SPECIFIED
import javax.inject.Inject

class ChartFragment : BaseObserveFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ChartViewModel
    private lateinit var destinationChangeListener: NavController.OnDestinationChangedListener
    private lateinit var selectedCurrency: String

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
        selectedCurrency = getChartCurrency()
        super.onViewCreated(view, savedInstanceState)

        configChart(selectedCurrency)
        setOnDestinationChangeListener()
        viewModel.subscribeOnCurrencyDataFlow(selectedCurrency)
    }

    private fun getChartCurrency(): String {
        val currencyParam = ChartFragmentArgs.fromBundle(requireArguments()).currency
        if (currencyParam != NOT_SPECIFIED) {
            showDebugLog("DEEP LINK PARAM:::: $currencyParam")
            return currencyParam
        } else {
            return DEFAULT_CURRENCY
        }
    }

    private fun configChart(selectedCurrency: String) {
        val chartConfig = LineDataSet(mutableListOf(), selectedCurrency).apply {
            lineWidth = 3f
            setDrawValues(false)
            setDrawCircles(false)
            color = resources.getColor(R.color.main_green)
        }

        binding.chart.data = LineData(chartConfig)

        with(binding.chart) {
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

                is StateChart.ShowErrorMessage -> {
                    showInfoDialog("Websocket Error", state.error)
                }

                StateChart.StartLoading -> {
                    binding.chart.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                StateChart.StopLoading -> {
                    binding.chart.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
                StateChart.OnConnectToWebSocket -> {
                    Toast.makeText(requireContext(), "Connection established", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }


    private fun updateChart(itemList: List<Entry>) {
        val data = binding.chart.data

        for (i in itemList.indices) {
            data.getDataSetByIndex(0).addEntry(itemList[i])
        }

        data.notifyDataChanged()
        binding.chart.notifyDataSetChanged()

        binding.chart.moveViewTo(
            (data.entryCount).toFloat(),
            50f,
            YAxis.AxisDependency.LEFT
        )
    }

    private fun setOnDestinationChangeListener() {
        destinationChangeListener =
            NavController.OnDestinationChangedListener { controller, destination, arguments ->
                showDebugLog("Fragment_Destination_changed: ${destination.label}")
                if (destination.label != "ChartFragment") {
                    viewModel.unsubscribeFromCurrencyDataFlow()
                }
                binding.root.findNavController()
                    .removeOnDestinationChangedListener(destinationChangeListener)
            }
        binding.root.findNavController().addOnDestinationChangedListener(destinationChangeListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setBlankState()
    }
}