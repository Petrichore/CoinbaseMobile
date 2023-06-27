package com.stefanenko.coinbase.ui.fragment.chart.chartFilter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.FragmentCurrencyFilterBinding
import com.stefanenko.coinbase.domain.entity.ActiveCurrency
import com.stefanenko.coinbase.ui.base.BaseObserveFragment
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import com.stefanenko.coinbase.ui.base.decorators.GridItemDecorator
import com.stefanenko.coinbase.ui.fragment.chart.chartFilter.recycler.AdapterActiveCurrency
import com.stefanenko.coinbase.util.toDp
import javax.inject.Inject

class CurrencyFilterFragment : BaseObserveFragment() {

    companion object {
        const val FILTER_NAV_RESULT_KEY = "FILTER_NAV_RESULT_KEY"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModelCurrency: CurrencyFilterViewModel

    private var _binding: FragmentCurrencyFilterBinding? = null
    private val binding: FragmentCurrencyFilterBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelCurrency.getActiveCurrency()
    }

    override fun initViewModel() {
        viewModelCurrency = ViewModelProvider(this, viewModelFactory)[CurrencyFilterViewModel::class.java]
    }

    override fun initObservers() {
        viewModelCurrency.state.observe(viewLifecycleOwner) {
            when (it) {
                is StateCurrencyFilter.ShowCurrencyRecycler -> initRecycler(it.itemList)
                is StateCurrencyFilter.ShowErrorMessage -> showInfoDialog(
                    resources.getString(R.string.alert_dialog_title_error),
                    it.error
                )
                StateCurrencyFilter.StartLoading -> binding.progressBar.visibility = View.VISIBLE
                StateCurrencyFilter.StopLoading -> binding.progressBar.visibility = View.GONE
                else -> Unit
            }
        }
    }

    private fun initRecycler(itemList: List<ActiveCurrency>) {
        with(binding.activeCurrencyRecycler) {
            layoutManager = GridLayoutManager(context, 2)
            adapter = AdapterActiveCurrency(itemList) {
                findNavController().navigate(CurrencyFilterFragmentDirections.actionFilterFragmentToChart(it.name))
            }
            addItemDecoration(GridItemDecorator(16.toDp()))
        }
    }
}