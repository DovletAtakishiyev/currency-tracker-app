package com.tshahakurov.currencytracker.data.model.converter

import com.tshahakurov.currencytracker.data.model.CustomCurrency
import com.tshahakurov.currencytracker.data.model.network.CurrencyListResponse

fun CurrencyListResponse.toCustomCurrencyArrayList(): ArrayList<CustomCurrency> {

    val customCurrencyList = ArrayList<CustomCurrency>()

    rates.forEach { (code, rate) ->
        val coefficient = rate.toFloatOrNull()
        if (coefficient != null) {
            val customCurrency = CustomCurrency(code, coefficient)
            customCurrencyList.add(customCurrency)
        }
    }

    customCurrencyList.sortBy { it.code }

    return customCurrencyList
}