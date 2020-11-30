package com.stefanenko.coinbase.ui.fragment.exchangeRate.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.domain.entity.ExchangeRate

class AdapterExchangeRate(
    private var itemList: List<ExchangeRate>,
    private val onItemCLickListener: (ExchangeRate) -> Unit
) : RecyclerView.Adapter<CurrencyExchangeRateViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrencyExchangeRateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_currency_exchange_rate, parent, false)
        return CurrencyExchangeRateViewHolder(view, onItemCLickListener)
    }

    override fun onBindViewHolder(holder: CurrencyExchangeRateViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun onUpdateAllItems(nItemList: List<ExchangeRate>){
        itemList = nItemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = itemList.size
}