package com.tshahakurov.currencytracker.app

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.tshahakurov.currencytracker.app.logic.network.NetworkReceiver
import com.tshahakurov.currencytracker.app.logic.work.DailyTaskWorker
import com.tshahakurov.currencytracker.ui.navigation.CurrencyApp
import com.tshahakurov.currencytracker.ui.theme.CurrencyTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- --- --- --- --- Work Manager  --- --- --- --- --- //
        createWorker()

        // --- --- --- --- --- Network Receiver  --- --- --- --- --- //
        registerReceiver(
            NetworkReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        // --- --- --- --- --- Screen Layout  --- --- --- --- --- //
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            CurrencyTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    CurrencyApp()
                }

            }
        }
    }

    private fun createWorker(){
        val workManager = WorkManager.getInstance(applicationContext)
        val uniqueWorkName = DailyTaskWorker.uniqueWorkName
        val workInfoListenableFuture = workManager.getWorkInfosForUniqueWork(uniqueWorkName)
        workInfoListenableFuture.addListener({
            val workStatuses = workInfoListenableFuture.get()
            if (workStatuses.isNullOrEmpty()) {
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                val workRequest = PeriodicWorkRequestBuilder<DailyTaskWorker>(1, TimeUnit.DAYS)
                    .setConstraints(constraints)
                    .build()
                workManager.enqueueUniquePeriodicWork(
                    uniqueWorkName,
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
            }
        }, ContextCompat.getMainExecutor(applicationContext))
    }
}