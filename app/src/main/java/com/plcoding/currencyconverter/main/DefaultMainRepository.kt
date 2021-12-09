package com.plcoding.currencyconverter.main

import com.plcoding.currencyconverter.data.CurrencyApi
import com.plcoding.currencyconverter.data.models.CurrencyResponse
import com.plcoding.currencyconverter.data.models.DataSound
import com.plcoding.currencyconverter.util.Resource
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(val api:CurrencyApi) :MainRepository {
    override suspend fun getSounds(): Resource<List<DataSound>> {
        return try {
            val response = api.getSounds()
            val result = response.body()
            if(response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch(e: Exception) {
            Resource.Error(e.message ?: "An error occured")
        }
    }

    //override suspend fun postSound(sound: DataSound): Resource<DataSound> {
    //    TODO("Not yet implemented")
    //}

}