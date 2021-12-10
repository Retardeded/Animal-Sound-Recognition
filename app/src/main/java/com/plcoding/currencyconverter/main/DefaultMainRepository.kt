package com.plcoding.currencyconverter.main

import com.plcoding.currencyconverter.data.models.PowerSpectrumCoefficient
import com.plcoding.currencyconverter.data.models.SoundType
import com.plcoding.currencyconverter.data.models.SoundsFreqCoefficients
import com.plcoding.currencyconverter.data.models.SoundsTimeCoefficients
import com.plcoding.currencyconverter.server.CurrencyApi
import com.plcoding.currencyconverter.data.models.DataSound
import com.plcoding.currencyconverter.util.Resource
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(val api: CurrencyApi) :MainRepository {
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

    override suspend fun getTypes(): Resource<List<SoundType>> {
        return try {
            val response = api.getTypes()
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

    override suspend fun getSound(id: String): DataSound {
        val response = api.getSound(id)
        val sound = response.body()!!
        println(":::::::::::::$sound")
        println(":::::::::::::$sound")
        println(":::::::::::::$sound")
       return sound
    }

    override suspend fun postSound(sound: DataSound): Resource<DataSound> {
        return try {
            val response = api.postSound(sound)
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

    override suspend fun deleteSound(id: String): Resource<Any> {
        return try {
            val response = api.deleteSound(id)
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

    override suspend fun checkSoundTimeDomain(sound: DataSound): Resource<List<Pair<SoundType, SoundsTimeCoefficients>>> {
        return try {
            val response = api.checkSoundTimeDomain(sound)
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

    override suspend fun checkSoundPowerSpectrum(sound: DataSound): Resource<List<Pair<SoundType, PowerSpectrumCoefficient>>> {
        return try {
            val response = api.checkSoundPowerSpectrum(sound)
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

    override suspend fun checkSoundFrequencyDomain(sound: DataSound): Resource<List<Pair<SoundType, SoundsFreqCoefficients>>> {
        return try {
            val response = api.checkSoundFrequencyDomain(sound)
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

}