package com.tshahakurov.currencytracker.data.model

import java.util.Currency

data class CustomCurrency(
    val code: String,
    val coefficient: Float
) {

    fun getSymbol(): String {
        return Currency.getInstance(code).symbol
    }

    fun calculateAmount(fromCurrency: CustomCurrency, amount: Float): Float {
        return (amount / fromCurrency.coefficient) * coefficient
    }

    override fun toString(): String {
        return Currency.getInstance(code).displayName
    }

    companion object {
        val defaultCurrency = CustomCurrency("EUR", 1f)
    }
}