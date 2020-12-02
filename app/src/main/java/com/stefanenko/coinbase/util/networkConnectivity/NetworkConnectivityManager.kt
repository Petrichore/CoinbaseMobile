package com.stefanenko.coinbase.util.networkConnectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectivityManager @Inject constructor(private val appContext: Context) {

    private val cm =
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun isConnected(): Boolean {
        val networkCapabilities = getNetworkInfo()
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }

    fun regNetworkCallBack(
        onAvailable: (Network) -> Unit,
        onLost: (Network) -> Unit
    ): ConnectivityManager.NetworkCallback {
        val callBack = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d("NETWORK STATE:::", "AVAILABLE")
                onAvailable.invoke(network)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.d("NETWORK STATE:::", "LOST")
                onLost.invoke(network)
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
        return cm.getNetworkCapabilities(activeNetwork)
    }
}