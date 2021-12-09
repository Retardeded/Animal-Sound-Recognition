package com.plcoding.currencyconverter.main

import com.plcoding.currencyconverter.data.models.CurrencyResponse
import com.plcoding.currencyconverter.data.models.DataSound
import com.plcoding.currencyconverter.util.Resource

interface MainRepository {

    suspend fun getSounds(): Resource<List<DataSound>>
}