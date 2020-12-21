package com.stefanenko.coinbase.ui.fragment.chart.chartFilter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.FragmentChartBinding
import com.stefanenko.coinbase.databinding.FragmentChartFilterBinding
import com.stefanenko.coinbase.domain.entity.ActiveCurrency
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.base.BaseObserveFragment
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import com.stefanenko.coinbase.ui.base.decorators.GridItemDecorator
import com.stefanenko.coinbase.ui.fragment.chart.chartFilter.recycler.AdapterActiveCurrency
import com.stefanenko.coinbase.util.setNavigationResult
import com.stefanenko.coinbase.util.toDp
import javax.inject.Inject

class FilterFragment : BaseObserveFragment() {

    companion object{
        const val FILTER_NAV_RESULT_KEY = "FILTER_NAV_RESULT_KEY"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: FilterViewModel

    private var _binding: FragmentChartFilterBinding? = null
    private val binding: FragmentChartFilterBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).toolbar.title = resources.getString(R.string.title_filter)
        (activity as MainActivity).menuBottomView.visibility = View.GONE
        viewModel.getActiveCurrency()
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[FilterViewModel::class.java]
    }

    override fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner, {
            when (it) {
                is StateFilter.ShowCurrencyRecycler -> initRecycler(it.itemList)
                StateFilter.StartLoading -> binding.progressBar.visibility = View.VISIBLE
                StateFilter.StopLoading -> binding.progressBar.visibility = View.GONE
            }
        })

        viewModel.stateScattering.observe(viewLifecycleOwner, {
            when (it) {
                is StateScattering.ShowErrorMessage -> showInfoDialog("Error", it.error)
            }
        })
    }

    private fun initRecycler(itemList: List<ActiveCurrency>) {
        with(binding.activeCurrencyRecycler) {
            layoutManager = GridLayoutManager(context, 2)
            adapter = AdapterActiveCurrency(itemList) {
                setNavigationResult(it.name, FILTER_NAV_RESULT_KEY)
                requireActivity().onBackPressed()
            }
            addItemDecoration(GridItemDecorator(16.toDp()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).menuBottomView.visibility = View.VISIBLE
    }
}