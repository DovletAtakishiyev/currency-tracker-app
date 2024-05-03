package com.tshahakurov.currencytracker.data.model

import android.net.Uri

data class UserData(
    val email: String,
    var name: String?,
    val imageUri: Uri?,
    val activeCurrencies: ArrayList<CustomCurrency> = arrayListOf()
){
    companion object{
        val defaultUser: UserData = UserData("guest", "guest", null)
    }
}