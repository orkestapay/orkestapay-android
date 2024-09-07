package com.orkestapay.orkestapay.core.networking

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkUtils(private val context: Context) {
    val hasInternet: Boolean
        get() {
            val cmg = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            cmg.getNetworkCapabilities(cmg.activeNetwork)?.let { networkCapabilities ->
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
            return false
        }
}