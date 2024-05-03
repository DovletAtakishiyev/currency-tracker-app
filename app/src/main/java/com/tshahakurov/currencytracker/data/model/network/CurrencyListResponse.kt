package com.tshahakurov.currencytracker.data.model.network

import com.google.gson.annotations.SerializedName

data class CurrencyListResponse (
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("timestamp")
    val timestamp: Int,
    @SerializedName("base")
    val base: String,
    @SerializedName("rates")
    val rates: HashMap<String, String>,
)