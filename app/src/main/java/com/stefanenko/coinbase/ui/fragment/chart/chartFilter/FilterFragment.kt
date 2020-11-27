package com.stefanenko.coinbase.ui.fragment.chart.chartFilter

import androidx.lifecycle.ViewModelProvider
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.ui.base.BaseObserveFragment
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import javax.inject.Inject

class FilterFragment : BaseObserveFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: FilterViewModel

    override fun getLayoutId(): Int = R.layout.fragment_chart_filter

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[FilterViewModel::class.java]
    }

    override fun initObservers() {

    }
}