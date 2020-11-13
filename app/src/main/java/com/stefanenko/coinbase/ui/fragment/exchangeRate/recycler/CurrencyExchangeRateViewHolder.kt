package com.stefanenko.coinbase.ui.fragment.exchangeRate.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import kotlinx.android.synthetic.main.item_currency_exchange_rate.view.*

class CurrencyExchangeRateViewHolder(
    private val view: View,
    private val onItemCLickListener: (ExchangeRate) -> Unit
) :
    RecyclerView.ViewHolder(view) {

    val currency = view.currencyName
    val exchangeRate = view.currencyExchangeRate

    fun bind(item: ExchangeRate) {
        currency.text = item.currencyName
        exchangeRate.text = item.exchangeRate.toString()

        view.setOnClickListener {
            onItemCLickListener.invoke(item)
        }
    }
}