package com.stefanenko.coinbase.ui.fragment.exchangeRate.recycler

import androidx.recyclerview.widget.RecyclerView
import com.stefanenko.coinbase.databinding.ItemCurrencyExchangeRateBinding
import com.stefanenko.coinbase.domain.entity.ExchangeRate

class CurrencyExchangeRateViewHolder(
    private val binding: ItemCurrencyExchangeRateBinding,
    private val onItemCLickListener: (ExchangeRate) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    private val currency = binding.currencyName
    private val exchangeRate = binding.currencyExchangeRate
    private val date = binding.exchangeRateDateValue
    private val time = binding.exchangeRateTimeValue

    fun bind(item: ExchangeRate) {
        currency.text = "1 ${item.currencyName} = "
        exchangeRate.text = "${item.exchangeRate} ${item.baseCurrency} "
        date.text = item.date
        time.text = item.time

        binding.root.setOnLongClickListener {
            onItemCLickListener.invoke(item)
            return@setOnLongClickListener true
        }
    }
}