package com.tshahakurov.currencytracker.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val USERS_TABLE_NAME = "users"
const val COLUMN_NAME = "username"
const val COLUMN_EMAIL = "email"
const val COLUMN_IMAGE_URI = "image_uri"
const val COLUMN_ACTIVE_CURRENCIES = "currencies"

@Entity(USERS_TABLE_NAME)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_EMAIL)
    val email: String,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String?,
    @ColumnInfo(name = COLUMN_IMAGE_URI)
    val imageUri: String?,
    @ColumnInfo(name = COLUMN_ACTIVE_CURRENCIES)
    val activeCurrenciesString: String?
)