package com.plcoding.currencyconverter.data

import com.plcoding.currencyconverter.data.models.DataSound
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApi {

    @GET("api/sounds/soundsInfo")
    suspend fun getSounds(): Response<List<DataSound>>
}