package com.tshahakurov.currencytracker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.tshahakurov.currencytracker.data.model.entity.COLUMN_EMAIL
import com.tshahakurov.currencytracker.data.model.entity.USERS_TABLE_NAME
import com.tshahakurov.currencytracker.data.model.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM $USERS_TABLE_NAME WHERE $COLUMN_EMAIL = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?
}