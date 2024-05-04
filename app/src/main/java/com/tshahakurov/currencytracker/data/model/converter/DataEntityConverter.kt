package com.tshahakurov.currencytracker.data.model.converter

import android.net.Uri
import com.tshahakurov.currencytracker.data.model.CustomCurrency
import com.tshahakurov.currencytracker.data.model.UserData
import com.tshahakurov.currencytracker.data.model.entity.CurrencyRatesEntity
import com.tshahakurov.currencytracker.data.model.entity.UserEntity

fun ArrayList<CustomCurrency>.toStringRepresentation(): String {
    return this.joinToString(separator = ",") { "${it.code}:${it.coefficient}" }
}

fun ArrayList<CustomCurrency>.toCurrencyRatesEntity(): CurrencyRatesEntity{
    return CurrencyRatesEntity(id = null, rates = this.toStringRepresentation())
}

fun String.toArrayListRepresentation(): ArrayList<CustomCurrency> {
    if (this.isEmpty() ) {
        return arrayListOf()
    }

    val currencies = ArrayList<CustomCurrency>()
    val currencyStrings = this.split(",")

    for (currencyString in currencyStrings) {
        val parts = currencyString.split(":")
        if (parts.size == 2) {
            val code = parts[0]
            val coefficient = parts[1].toFloatOrNull()
            if (coefficient != null) {
                currencies.add(CustomCurrency(code, coefficient))
            }
        }
    }

    return currencies.ifEmpty { arrayListOf() }
}

fun UserData.toUserEntity(): UserEntity {
    val activeCurrenciesString = activeCurrencies.toStringRepresentation()
    return UserEntity(email, name, imageUri.toString(), activeCurrenciesString)
}

fun UserEntity.toUserData(): UserData {
    return UserData(
        email,
        name,
        imageUri?.let { Uri.parse(it) },
        activeCurrenciesString?.toArrayListRepresentation() ?: arrayListOf()
    )
}