package com.tshahakurov.currencytracker.data.repository

import com.tshahakurov.currencytracker.app.logic.network.CurrencyApi
import com.tshahakurov.currencytracker.data.db.CurrencyDao
import com.tshahakurov.currencytracker.data.model.CustomCurrency
import com.tshahakurov.currencytracker.data.model.converter.toArrayListRepresentation
import com.tshahakurov.currencytracker.data.model.converter.toCurrencyRatesEntity
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val currencyApi: CurrencyApi,
    private val currencyDao: CurrencyDao
) {

    suspend fun getLatestRates() = currencyApi.getLatestRates()

    suspend fun saveRates(rates: ArrayList<CustomCurrency>) {
        currencyDao.insertCurrencies(rates.toCurrencyRatesEntity())
    }

    suspend fun getLastSavedRates(): ArrayList<CustomCurrency> {
        return currencyDao.getLastSavedRates()?.rates?.toArrayListRepresentation()
            ?: arrayListOf()
    }
}