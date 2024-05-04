package com.tshahakurov.currencytracker.data.repository

import com.tshahakurov.currencytracker.data.db.UserDao
import com.tshahakurov.currencytracker.data.model.UserData
import com.tshahakurov.currencytracker.data.model.converter.toUserData
import com.tshahakurov.currencytracker.data.model.converter.toUserEntity
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
){

    suspend fun insertUser(user: UserData) {
        userDao.insertUser(user.toUserEntity())
    }

    suspend fun updateUser(user: UserData) {
        userDao.updateUser(user.toUserEntity())
    }

    suspend fun getUserByEmail(email: String): UserData? {
        return userDao.getUserByEmail(email)?.toUserData()
    }
}