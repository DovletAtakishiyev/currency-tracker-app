package com.tshahakurov.currencytracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tshahakurov.currencytracker.data.model.entity.CurrencyRatesEntity
import com.tshahakurov.currencytracker.data.model.entity.UserEntity

const val CURRENCY_DB_NAME = "currency_app_db"

@Database(entities = [UserEntity::class, CurrencyRatesEntity::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun currencyDao(): CurrencyDao
}