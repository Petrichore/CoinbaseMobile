package com.stefanenko.coinbase.ui.fragment.favorites.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.databinding.ItemCurrencyExchangeRateBinding
import com.stefanenko.coinbase.domain.entity.ExchangeRate

class AdapterFavorites(
    private var itemList: List<ExchangeRate>,
    private val onItemCLickListener: (ExchangeRate) -> Unit
) : RecyclerView.Adapter<FavoritesViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesViewHolder {
        val binding = ItemCurrencyExchangeRateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(binding, onItemCLickListener)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    fun onDeleteItem(position: Int){
        val itemMutableList = mutableListOf<ExchangeRate>().apply { addAll(itemList) }
        itemMutableList.removeAt(position)
        itemList = itemMutableList.toList()
        notifyItemRemoved(position)
    }

    fun onInsertItem(position: Int, item: ExchangeRate){
        val itemMutableList = mutableListOf<ExchangeRate>().apply { addAll(itemList) }
        itemMutableList.add(position, item)
        itemList = itemMutableList.toList()
        notifyItemInserted(position)
    }

    fun getItemByPosition(position: Int): ExchangeRate{
        return itemList[position]
    }
}