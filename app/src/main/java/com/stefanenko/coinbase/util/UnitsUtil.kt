package com.stefanenko.coinbase.util

import android.content.res.Resources
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

fun Int.toDp(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

// TODO Should be deleted, just check
fun dateFormatter(): String {

    val tf = SimpleDateFormat("hh:mma", Locale.US).apply {
        dateFormatSymbols = DateFormatSymbols(Locale.US).apply {
            amPmStrings = arrayOf("am", "pm")
        }
    }
    val date: Date? = null
    return tf.format(date)
}