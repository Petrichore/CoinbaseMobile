package com.stefanenko.coinbase.domain.util

import java.util.*

object DateManager {

    fun getCurrentDateAsString(): String {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return if (month < 10) "$year-0$month-$day" else "$year-$month-$day"
    }

    fun getCurrentTimeAsString(): String {
        val calendar = Calendar.getInstance()

        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val min = calendar.get(Calendar.MINUTE)
        val sec = calendar.get(Calendar.SECOND)

        return if (min < 10) "$hours:0$min:$sec" else "$hours:$min:$sec"
    }
}