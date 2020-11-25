package com.stefanenko.coinbase.ui.fragment.favorites.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stefanenko.coinbase.R
import com.stefanenko.coinbase.domain.entity.ExchangeRate

class AdapterFavorites(
    private val itemList: List<ExchangeRate>,
    private val onItemCLickListener: (ExchangeRate) -> Unit
) : RecyclerView.Adapter<FavoritesViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_currency_exchange_rate, parent, false)
        return FavoritesViewHolder(view, onItemCLickListener)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size
}