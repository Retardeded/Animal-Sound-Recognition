package com.plcoding.soundrecognition.viewmodels

import com.plcoding.soundrecognition.data.models.PowerSpectrumCoefficient
import com.plcoding.soundrecognition.data.models.SoundType
import com.plcoding.soundrecognition.data.models.SoundsFreqCoefficients
import com.plcoding.soundrecognition.data.models.SoundsTimeCoefficients
import com.plcoding.soundrecognition.data.models.DataSound
import com.plcoding.soundrecognition.util.Resource
import retrofit2.http.*

interface MainRepository {

    suspend fun getSounds(): Resource<List<DataSound>>

    suspend fun getTypes(): Resource<List<SoundType>>

    suspend fun getSound(@Path("id") id:String): DataSound

    suspend fun postSound(@Body sound: DataSound): Resource<DataSound>

    suspend fun deleteSound(@Path("id") id:String): Resource<Any>

    suspend fun checkSoundTimeDomain(@Body sound: DataSound): Resource<List<Pair<SoundType, SoundsTimeCoefficients>>>

    suspend fun checkSoundPowerSpectrum(@Body sound: DataSound): Resource<List<Pair<SoundType, PowerSpectrumCoefficient>>>

    suspend fun checkSoundFrequencyDomain(@Body sound: DataSound): Resource<List<Pair<SoundType, SoundsFreqCoefficients>>>

}