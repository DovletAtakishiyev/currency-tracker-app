package com.tshahakurov.currencytracker.app.logic.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tshahakurov.currencytracker.data.model.CustomCurrency
import com.tshahakurov.currencytracker.data.model.converter.toCustomCurrencyArrayList
import com.tshahakurov.currencytracker.data.repository.CurrencyRepository

class DailyTaskWorker(
    context: Context,
    params: WorkerParameters,
    private val currencyRepository: CurrencyRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val currencyList: ArrayList<CustomCurrency>
        return try {
            val response = currencyRepository.getLatestRates()
            if (response.isSuccessful) {
                currencyList = response.body()?.toCustomCurrencyArrayList() ?: arrayListOf()
                currencyRepository.saveRates(currencyList)
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object{
        const val uniqueWorkName = "daily_task"
    }
}