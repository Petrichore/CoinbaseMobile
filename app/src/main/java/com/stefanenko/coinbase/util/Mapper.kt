package com.stefanenko.coinbase.util

import com.github.mikephil.charting.data.Entry
import com.stefanenko.coinbase.domain.entity.CurrencyMarketInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Mapper @Inject constructor() {

    fun map(itemList: List<CurrencyMarketInfo>, itemAmount: Float): List<Entry>{
        var counter = itemAmount
        val entryList = mutableListOf<Entry>()

        for (i in itemList.indices) {
            entryList.add(Entry(counter++, itemList[i].price))
        }

        return entryList
    }
}