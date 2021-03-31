package com.stefanenko.coinbase.util.networkConnectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class NetworkStateReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    }
}