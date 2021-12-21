package com.plcoding.soundrecognition.viewmodels

import com.plcoding.soundrecognition.data.models.PowerSpectrumCoefficient
import com.plcoding.soundrecognition.data.models.SoundType
import com.plcoding.soundrecognition.data.models.SoundsFreqCoefficients
import com.plcoding.soundrecognition.data.models.SoundsTimeCoefficients
import com.plcoding.soundrecognition.server.SoundService
import com.plcoding.soundrecognition.data.models.DataSound
import com.plcoding.soundrecognition.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(val api: SoundService) :MainRepository {
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

    override suspend fun getSound(id: String): Flow<Resource<DataSound>> = flow {
        //emit(Resource.Loading())

        //val wordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        var dumData = DataSound("", "", 0, 0, arrayListOf())

        try {
            val remoteWordInfos = api.getSound(id)
            dumData = remoteWordInfos.body()!!
            println(":::::::::::::$dumData")
            println(":::::::::::::$dumData")
            println(":::::::::::::$dumData")
        } catch(e: HttpException) {
            emit(Resource.Error(
                message = "Oops, something went wrong!",
                data = dumData
            ))
        } catch(e: IOException) {
            emit(Resource.Error(
                message = "Couldn't reach server, check your internet connection.",
                data = dumData
            ))
        }

        //val newWordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Success(dumData))
    }


    /*
    override suspend fun getSound(id: String): Flow<Resource<DataSound>> {

        /*
        val response = api.getSound(id)
        val sound = response.body()!!
        println(":::::::::::::$sound")
        println(":::::::::::::$sound")
        println(":::::::::::::$sound")
       return sound

         */

        return try {
            val response = api.getSound(id)
            val result = response.body()

            println(":::::::::::::$result")
            println(":::::::::::::$result")
            println(":::::::::::::$result")
            if(response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch(e: Exception) {
            Resource.Error(e.message ?: "An error occured")
        }

    }

     */

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