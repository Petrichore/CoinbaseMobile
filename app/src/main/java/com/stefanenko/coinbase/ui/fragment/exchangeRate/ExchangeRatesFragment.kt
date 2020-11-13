package com.stefanenko.coinbase.ui.fragment.exchangeRate

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.base.BaseObserveFragment
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import com.stefanenko.coinbase.ui.base.decorators.VerticalItemDecoration
import com.stefanenko.coinbase.ui.fragment.exchangeRate.recycler.AdapterExchangeRate
import com.stefanenko.coinbase.util.toDp
import kotlinx.android.synthetic.main.fragment_exchange_rate.*
import javax.inject.Inject

class ExchangeRatesFragment : BaseObserveFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ExchangeRatesViewModel

    private lateinit var recyclerAdapter: AdapterExchangeRate

    override fun getLayoutId(): Int = R.layout.fragment_exchange_rate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).toolbar.title =
            resources.getString(R.string.toolbar_title_exchange_rate)

        viewModel.getExchangeRates("USD")
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[ExchangeRatesViewModel::class.java]
    }

    private fun initRecycler(itemList: List<ExchangeRate>) {
        Log.d("ItemList", "$itemList")
        with(exchangeRatesRecycler) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerAdapter = AdapterExchangeRate(itemList) {

            }

            adapter = recyclerAdapter
            addItemDecoration(VerticalItemDecoration(14.toDp()))
        }
    }

    override fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner, {
            when (it) {
                is StateExchangeRates.ShowExchangeRateRecycler -> initRecycler(it.itemList)

                is StateExchangeRates.UpdateExchangeRateRecycler -> { }

                is StateExchangeRates.ShowErrorMessage -> showInfoDialog("Error", it.error)

            }
        })

        viewModel.interruptibleState.observe(viewLifecycleOwner, {
            when (it) {
                is InterruptibleState.StartLoading -> progressBar.visibility = View.VISIBLE
                is InterruptibleState.StopLoading -> progressBar.visibility = View.GONE
            }
        })
    }
}