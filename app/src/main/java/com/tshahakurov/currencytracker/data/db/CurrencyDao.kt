package com.tshahakurov.currencytracker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tshahakurov.currencytracker.data.model.entity.CURRENCY_TABLE_NAME
import com.tshahakurov.currencytracker.data.model.entity.CurrencyRatesEntity

@Dao
interface CurrencyDao {
    @Insert
    suspend fun insertCurrencies(currencies: CurrencyRatesEntity)

    @Query("SELECT * FROM $CURRENCY_TABLE_NAME ORDER BY id DESC LIMIT 1")
    suspend fun getLastSavedRates(): CurrencyRatesEntity?
}