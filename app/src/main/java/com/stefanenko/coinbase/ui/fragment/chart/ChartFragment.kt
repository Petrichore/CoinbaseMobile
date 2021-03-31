package com.stefanenko.coinbase.ui.fragment.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.FragmentChartBinding
import com.stefanenko.coinbase.ui.base.BaseObserveFragment
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import com.stefanenko.coinbase.ui.fragment.chart.ChartViewModel.Companion.DEFAULT_CURRENCY
import com.stefanenko.coinbase.ui.fragment.chart.ChartViewModel.Companion.NOT_SPECIFIED
import javax.inject.Inject

class ChartFragment : BaseObserveFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ChartViewModel
    private lateinit var destinationChangeListener: NavController.OnDestinationChangedListener
    private lateinit var selectedCurrency: String

    private var _binding: FragmentChartBinding? = null
    private val binding: FragmentChartBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = false
    }

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
        selectedCurrency = getChartCurrency()

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

        binding.chartView.data = LineData(chartConfig)

        with(binding.chartView) {
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
                    showInfoDialog(
                        resources.getString(R.string.alert_dialog_title_error),
                        state.error
                    )
                }

                StateChart.StartLoading -> {
                    binding.chartView.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }

                StateChart.StopLoading -> {
                    binding.chartView.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }

                StateChart.OnConnectToWebSocket -> {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.toast_test_connected_to_webSocket),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun updateChart(itemList: List<Entry>) {
        val data = binding.chartView.data

        for (i in itemList.indices) {
            data.getDataSetByIndex(0).addEntry(itemList[i])
        }

        data.notifyDataChanged()
        binding.chartView.notifyDataSetChanged()

        binding.chartView.moveViewTo(
            (data.entryCount).toFloat(),
            50f,
            YAxis.AxisDependency.LEFT
        )
    }

    private fun setOnDestinationChangeListener() {
        destinationChangeListener =
            NavController.OnDestinationChangedListener { _, destination, _ ->
                if (destination.label != resources.getString(R.string.nav_label_chart)) {
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