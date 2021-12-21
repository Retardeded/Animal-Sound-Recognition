package com.plcoding.soundrecognition.server

import com.plcoding.soundrecognition.data.models.*
import com.plcoding.soundrecognition.util.Resource
import javax.inject.Inject

class SoundServiceHandler @Inject constructor(private val api: SoundService) {

    suspend fun getSound(id:String): Resource<DataSound> {

        return try {
            val response = api.getSound(id)
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


    suspend fun deleteSound(id:String): Resource<Any> {
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


    suspend fun getSounds(): Resource<List<DataSound>> {
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

    suspend fun getTypes(): Resource<List<SoundType>> {
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

    suspend fun postSound(sound: DataSound): Resource<DataSound> {
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

    suspend fun checkSoundTimeDomain(sound: DataSound): Resource<List<Pair<SoundType, SoundsTimeCoefficients>>> {
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

    suspend fun checkSoundPowerSpectrum(sound: DataSound): Resource<List<Pair<SoundType, PowerSpectrumCoefficient>>> {
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

    suspend fun checkSoundFrequencyDomain(sound: DataSound): Resource<List<Pair<SoundType, SoundsFreqCoefficients>>> {
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