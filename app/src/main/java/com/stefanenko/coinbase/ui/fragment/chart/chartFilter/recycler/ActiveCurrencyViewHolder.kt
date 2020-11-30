package com.stefanenko.coinbase.ui.fragment.chart.chartFilter.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.stefanenko.coinbase.domain.entity.ActiveCurrency
import kotlinx.android.synthetic.main.item_active_currency.view.*

class ActiveCurrencyViewHolder(
    private val view: View,
    private val onItemClick: (ActiveCurrency) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val currencyName = view.currencyName

    fun bind(item: ActiveCurrency) {

        currencyName.text = item.name

        view.setOnClickListener {
            onItemClick(item)
        }
    }
}