package com.tshahakurov.currencytracker.app.logic.auth

import com.tshahakurov.currencytracker.data.model.UserData

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)
