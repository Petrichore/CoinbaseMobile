package com.stefanenko.coinbase.ui.fragment.exchangeRate

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.FragmentExchangeRateBinding
import com.stefanenko.coinbase.ui.base.BaseObserveFragment
import com.stefanenko.coinbase.ui.base.ViewModelFactory
import com.stefanenko.coinbase.ui.base.decorators.GridItemDecorator
import com.stefanenko.coinbase.ui.base.decorators.VerticalItemDecoration
import com.stefanenko.coinbase.ui.fragment.exchangeRate.ExchangeRatesViewModel.Companion.DEFAULT_BASE_CURRENCY
import com.stefanenko.coinbase.ui.fragment.exchangeRate.recycler.AdapterExchangeRate
import com.stefanenko.coinbase.util.toDp
import javax.inject.Inject

class ExchangeRatesFragment : BaseObserveFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ExchangeRatesViewModel

    private var _binding: FragmentExchangeRateBinding? = null
    private val binding: FragmentExchangeRateBinding
        get() = _binding!!

    private lateinit var recyclerAdapter: AdapterExchangeRate

    private lateinit var snackbar: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExchangeRateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configRecycler()
        configSnackBar()
        configSwipeToRefresh()
    }

    private fun configSnackBar() {
        snackbar =
            Snackbar.make(
                requireView(),
                resources.getString(R.string.snackBar_text_currency_saved),
                Snackbar.LENGTH_SHORT
            ).apply {
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.main_green))
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setActionTextColor(ContextCompat.getColor(context, R.color.color_secondary_variant))
                setAction(resources.getString(R.string.snackBar_positive_button_ok)) {
                    snackbar.dismiss()
                }
            }
    }

    private fun configRecycler() {
        binding.exchangeRatesRecycler.let {
            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    it.layoutManager = GridLayoutManager(context, 2)
                    it.addItemDecoration(GridItemDecorator(14.toDp()))
                }
                else -> {
                    it.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    it.addItemDecoration(VerticalItemDecoration(14.toDp()))
                }
            }

            recyclerAdapter = AdapterExchangeRate {
                viewModel.checkAbilityToSaveCurrency(it)
            }
            it.adapter = recyclerAdapter
        }
    }

    private fun configSwipeToRefresh() {
        binding.swipeToRefresh.setOnRefreshListener {
            if (::recyclerAdapter.isInitialized) {
                viewModel.updateExchangeRates(DEFAULT_BASE_CURRENCY)
            } else {
                viewModel.getExchangeRates(DEFAULT_BASE_CURRENCY)
            }
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[ExchangeRatesViewModel::class.java]
    }

    override fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner, {
            showDebugLog(it.toString())
            when (it) {
                is StateExchangeRates.ShowExchangeRateRecycler -> {
                    showDebugLog("SHOW RECYCLER")
                    recyclerAdapter.onUpdateAllItems(it.itemList)
                }

                is StateExchangeRates.UpdateExchangeRateRecycler -> {
                    showDebugLog("UPDATE RECYCLER")
                    recyclerAdapter.onUpdateAllItems(it.nItemList)
                }

                is StateExchangeRates.ShowErrorMessage -> {
                    showInfoDialog(resources.getString(R.string.alert_dialog_title_error), it.error)
                }

                is StateExchangeRates.ShowDialogSaveToFav -> {
                    showAlertDialog(
                        resources.getString(R.string.alert_dialog_title_add_to_favorite),
                        "Add ${it.exchangeRate.currencyName} to favorite?",
                        { _ ->
                            viewModel.addCurrencyToFavorite(it.exchangeRate)
                        },
                        { dialog ->
                            dialog.dismiss()
                        })
                }

                StateExchangeRates.NetworkAvailable -> {
                    showDebugLog("NETWORK AVAILABLE")
                    if (::recyclerAdapter.isInitialized) {
                        showDebugLog("Network Action: Update exchange rates")
                        viewModel.updateExchangeRates(DEFAULT_BASE_CURRENCY)
                    } else {
                        showDebugLog("Network Action: getExchangeRate")
                        viewModel.getExchangeRates(DEFAULT_BASE_CURRENCY)
                    }
                }

                StateExchangeRates.NetworkUnavailable -> {
                    showDebugLog("NETWORK UNAVAILABLE")
                    viewModel.getCashedExchangeRates()
                }
                StateExchangeRates.StartLoading -> {
                    binding.swipeToRefresh.isRefreshing = true
                    showDebugLog("START LOADING::::")
                }
                StateExchangeRates.StopLoading -> {
                    showDebugLog("STOP LOADING::::")
                    binding.swipeToRefresh.isRefreshing = false
                }

                StateExchangeRates.ShowDialogUserAuthMissing -> {
                    showInfoDialog(
                        resources.getString(R.string.alert_dialog_title_auth_missing),
                        resources.getString(R.string.alert_dialog_message_auth_messing),
                    )
                }

                StateExchangeRates.ShowSnackBar -> snackbar.show()

                StateExchangeRates.BlankState -> {
                }

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.setBlankState()
    }
}