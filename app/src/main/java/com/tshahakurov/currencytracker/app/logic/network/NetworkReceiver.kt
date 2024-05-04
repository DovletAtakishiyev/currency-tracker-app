package com.tshahakurov.currencytracker.app.logic.network

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import kotlinx.coroutines.flow.MutableStateFlow

object NetworkReceiver : BroadcastReceiver() {
    val isNetworkConnected = MutableStateFlow(false)
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        isNetworkConnected.value = context?.isNetworkConnected() == true
    }
}

fun Context.isNetworkConnected() =
    (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
        activeNetworkInfo?.isConnected == true
    }