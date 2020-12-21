package com.stefanenko.coinbase.ui.fragment.chart.chartFilter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.ItemActiveCurrencyBinding
import com.stefanenko.coinbase.domain.entity.ActiveCurrency

class AdapterActiveCurrency(
    private val itemList: List<ActiveCurrency>,
    private val onItemClick: (ActiveCurrency) -> Unit
): RecyclerView.Adapter<ActiveCurrencyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveCurrencyViewHolder {
        val binding = ItemActiveCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActiveCurrencyViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ActiveCurrencyViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size
}