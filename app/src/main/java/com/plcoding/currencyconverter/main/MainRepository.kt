package com.plcoding.currencyconverter.main

import com.plcoding.currencyconverter.data.models.PowerSpectrumCoefficient
import com.plcoding.currencyconverter.data.models.SoundType
import com.plcoding.currencyconverter.data.models.SoundsFreqCoefficients
import com.plcoding.currencyconverter.data.models.SoundsTimeCoefficients
import com.plcoding.currencyconverter.data.models.DataSound
import com.plcoding.currencyconverter.util.Resource
import retrofit2.Call
import retrofit2.Response
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