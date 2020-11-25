package com.stefanenko.coinbase.ui.fragment.favorites.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.stefanenko.coinbase.domain.entity.ExchangeRate
import kotlinx.android.synthetic.main.item_currency_exchange_rate.view.*

class FavoritesViewHolder(
    private val view: View,
    private val onItemCLickListener: (ExchangeRate) -> Unit
) :
    RecyclerView.ViewHolder(view) {

    private val currency = view.currencyName
    private val exchangeRate = view.currencyExchangeRate
    private val date = view.exchangeRateDateValue
    private val time = view.exchangeRateTimeValue

    fun bind(item: ExchangeRate) {
        currency.text = "1 ${item.currencyName} = "
        exchangeRate.text = "${item.exchangeRate} ${item.baseCurrency}"
        date.text = item.date
        time.text = item.time
    }
}