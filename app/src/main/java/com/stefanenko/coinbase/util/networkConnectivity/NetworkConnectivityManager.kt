package com.stefanenko.coinbase.util.networkConnectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectivityManager @Inject constructor(appContext: Context) {

    private val cm =
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun isConnected(): Boolean {
        val networkCapabilities = getNetworkInfo()
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }

    private val pevConnectivityType = ""
    private val wifiType = "wifi"
    private val cellularType = "cellular"

    fun regNetworkCallBack(
        onAvailable: (Network) -> Unit,
        onLost: (Network) -> Unit
    ): ConnectivityManager.NetworkCallback {
        val callBack = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d("NETWORKINFO", "AVAILABLE")
                onAvailable.invoke(network)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.d(
                    "NETWORKINFO",
                    "LOST(is connected:${isConnected()})\nALL NETWORKS: ${cm.allNetworks}"
                )
                if (!isConnected()) {
                    onLost.invoke(network)
                }
            }
        }
        cm.registerDefaultNetworkCallback(callBack)
        return callBack
    }

    fun revokeNetworkCallBack(callBack: ConnectivityManager.NetworkCallback) {
        cm.unregisterNetworkCallback(callBack)
    }

    private fun getNetworkInfo(): NetworkCapabilities? {
        val activeNetwork = cm.activeNetwork
        Log.d(
            "NETWORKINFO",
            "Active: \nWIFI:${
                cm.getNetworkCapabilities(activeNetwork)
                    ?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }\nMOBILE:${
                cm.getNetworkCapabilities(activeNetwork)
                    ?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            }"
        )
        return cm.getNetworkCapabilities(activeNetwork)
    }
}