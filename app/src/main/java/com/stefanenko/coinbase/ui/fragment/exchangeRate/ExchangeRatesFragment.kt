package com.stefanenko.coinbase.ui.fragment.exchangeRate

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
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

    private lateinit var snackbar: Snackbar

    override fun getLayoutId(): Int = R.layout.fragment_exchange_rate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configSnackBar()
        (activity as MainActivity).toolbar.title =
            resources.getString(R.string.toolbar_title_exchange_rate)

        viewModel.getExchangeRates("USD")
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

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[ExchangeRatesViewModel::class.java]
    }

    private fun initRecycler(itemList: List<ExchangeRate>) {
        Log.d("ItemList", "$itemList")
        with(exchangeRatesRecycler) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerAdapter = AdapterExchangeRate(itemList) {
                viewModel.checkAbilityToSaveCurrency(it)
            }

            adapter = recyclerAdapter
            addItemDecoration(VerticalItemDecoration(14.toDp()))
        }
    }

    override fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner, {
            when (it) {
                is StateExchangeRates.ShowExchangeRateRecycler -> initRecycler(it.itemList)

                is StateExchangeRates.UpdateExchangeRateRecycler -> {
                }

                is StateExchangeRates.StartLoading -> progressBar.visibility = View.VISIBLE
                is StateExchangeRates.StopLoading -> progressBar.visibility = View.GONE
            }
        })

        viewModel.scatteringState.observe(viewLifecycleOwner, {
            when (it) {
                is StateScattering.ShowErrorMessage -> showInfoDialog("Error", it.error)

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
                StateScattering.ShowSnackBar -> {
                    snackbar.show()
                }
            }
        })
    }
}