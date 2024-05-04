package com.tshahakurov.currencytracker.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENCY_TABLE_NAME = "currencies"
const val CURRENCY_TABLE_ID = "id"
const val CURRENCY_TABLE_RATES = "rates"

@Entity(CURRENCY_TABLE_NAME)
data class CurrencyRatesEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = CURRENCY_TABLE_ID)
    val id: Int?,
    @ColumnInfo(name = CURRENCY_TABLE_RATES)
    val rates: String?
)