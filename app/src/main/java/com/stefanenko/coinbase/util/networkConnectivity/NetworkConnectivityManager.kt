package com.stefanenko.coinbase.util.networkConnectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectivityManager @Inject constructor(private val appContext: Context) {

    fun isConnected(): Boolean {
        val networkCapabilities = getNetworkInfo()
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }

    private fun getNetworkInfo(): NetworkCapabilities? {
        val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetwork

        return cm.getNetworkCapabilities(activeNetwork)
    }
}