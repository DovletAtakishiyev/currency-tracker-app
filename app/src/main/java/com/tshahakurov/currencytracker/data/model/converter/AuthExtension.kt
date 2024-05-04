package com.tshahakurov.currencytracker.data.model.converter

import com.google.firebase.auth.FirebaseAuth
import com.tshahakurov.currencytracker.data.model.UserData

const val GUEST = "guest"

fun FirebaseAuth.toUserData() : UserData {
    return UserData(
        name = currentUser?.displayName ?: GUEST,
        email = currentUser?.email ?: GUEST,
        imageUri = currentUser?.photoUrl
    )
}