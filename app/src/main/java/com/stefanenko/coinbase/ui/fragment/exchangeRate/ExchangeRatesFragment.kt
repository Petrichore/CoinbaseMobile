package com.stefanenko.coinbase.ui.fragment.exchangeRate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.FragmentExchangeRateBinding
import com.stefanenko.coinbase.ui.activity.appMain.MainActivity
import com.stefanenko.coinbase.ui.base.BaseObserveFragment
import com.stefanenko.coinbase.ui.base.ViewModelFactory
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

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (sharedViewModel.getCurrency().isNotEmpty()) {
//            findNavController().navigate(R.id.action_products_to_chart)
//        }
//    }

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
        initRecycler()
        configSnackBar()
        configSwipeToRefresh()
        (activity as MainActivity).toolbar.title =
            resources.getString(R.string.toolbar_title_exchange_rate)
    }

    private fun configSnackBar() {
        snackbar =
            Snackbar.make(requireView(), "Currency successfully saved", Snackbar.LENGTH_SHORT)
                .apply {
                    this.view.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.main_green
                        )
                    )
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    setAction("OK") { snackbar.dismiss() }
                    setActionTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.color_secondary_variant
                        )
                    )
                }
    }

    private fun initRecycler() {
        with(binding.exchangeRatesRecycler) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerAdapter = AdapterExchangeRate {
                viewModel.checkAbilityToSaveCurrency(it)
            }
            adapter = recyclerAdapter
            addItemDecoration(VerticalItemDecoration(14.toDp()))
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
            }
        })

        viewModel.stateScattering.observe(viewLifecycleOwner, {
            when (it) {
                is StateScattering.ShowErrorMessage -> showInfoDialog("Error", it.error)

                StateScattering.ShowSnackBar -> snackbar.show()

                StateScattering.ShowDialogUserAuthMissing -> {
                    showInfoDialog(
                        "Auth missing",
                        "Please, authorize to be able to save data locally"
                    )
                }

                is StateScattering.ShowDialogSaveToFav -> {
                    showAlertDialog(
                        "Add to favorite",
                        "Add ${it.exchangeRate.currencyName} to favorite?",
                        { dialog ->
                            viewModel.addCurrencyToFavorite(it.exchangeRate)
                        },
                        { dialog ->
                            dialog.dismiss()
                        })
                }

                is StateScattering.StartLoading -> {
                    binding.swipeToRefresh.isRefreshing = true
                    showDebugLog("START LOADING::::")
                }
                is StateScattering.StopLoading -> {
                    showDebugLog("STOP LOADING::::")
                    binding.swipeToRefresh.isRefreshing = false
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.scatterStates()
    }
}