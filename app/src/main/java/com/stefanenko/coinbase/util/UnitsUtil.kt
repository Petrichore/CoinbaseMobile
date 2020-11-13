package com.stefanenko.coinbase.util

import android.content.res.Resources

fun Int.toDp(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()