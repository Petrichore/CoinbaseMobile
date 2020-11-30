package com.stefanenko.coinbase.ui.fragment.chart.chartFilter

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.domain.entity.ActiveCurrency
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.base.BaseObserveFragment
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import com.stefanenko.coinbase.ui.base.decorators.GridItemDecorator
import com.stefanenko.coinbase.ui.fragment.chart.chartFilter.recycler.AdapterActiveCurrency
import com.stefanenko.coinbase.util.setNavigationResult
import com.stefanenko.coinbase.util.toDp
import kotlinx.android.synthetic.main.fragment_chart_filter.*
import javax.inject.Inject

class FilterFragment : BaseObserveFragment() {

    companion object{
        const val FILTER_NAV_RESULT_KEY = "FILTER_NAV_RESULT_KEY"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: FilterViewModel

    override fun getLayoutId(): Int = R.layout.fragment_chart_filter

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
                StateFilter.StartLoading -> progressBar.visibility = View.VISIBLE
                StateFilter.StopLoading -> progressBar.visibility = View.GONE
            }
        })

        viewModel.stateScattering.observe(viewLifecycleOwner, {
            when (it) {
                is StateScattering.ShowErrorMessage -> showInfoDialog("Error", it.error)
            }
        })
    }

    private fun initRecycler(itemList: List<ActiveCurrency>) {
        with(activeCurrencyRecycler) {
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