package com.stefanenko.coinbase.ui.fragment.exchangeRate.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stefanenko.coinbase.R

class AdapterExchangeRate : RecyclerView.Adapter<CurrencyExchangeRateViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrencyExchangeRateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency_exchange_rate, parent, false)
        return CurrencyExchangeRateViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyExchangeRateViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 0
}