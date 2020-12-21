package com.stefanenko.coinbase.ui.fragment.chart.chartFilter.recycler

import androidx.recyclerview.widget.RecyclerView
import com.stefanenko.coinbase.databinding.ItemActiveCurrencyBinding
import com.stefanenko.coinbase.domain.entity.ActiveCurrency

class ActiveCurrencyViewHolder(
    private val binding: ItemActiveCurrencyBinding,
    private val onItemClick: (ActiveCurrency) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val currencyName = binding.currencyName

    fun bind(item: ActiveCurrency) {

        currencyName.text = item.name

        binding.root.setOnClickListener {
            onItemClick(item)
        }
    }
}