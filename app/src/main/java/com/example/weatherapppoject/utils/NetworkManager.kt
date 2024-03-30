package com.example.weatherapppoject.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log

object NetworkManager {


//    fun isNetworkAvailable(context: Context): Boolean {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        val network = connectivityManager.activeNetwork ?: return false
//        val networkCapabilities =
//            connectivityManager.getNetworkCapabilities(network) ?: return false
//
//        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
//                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
//    }
}