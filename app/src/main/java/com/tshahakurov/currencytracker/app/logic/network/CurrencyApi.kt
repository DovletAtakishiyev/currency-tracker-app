package com.tshahakurov.currencytracker.app.logic.network

import com.tshahakurov.currencytracker.data.model.network.CurrencyListResponse
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApi {

    @GET("latest")
    suspend fun getLatestRates(): Response<CurrencyListResponse>
}