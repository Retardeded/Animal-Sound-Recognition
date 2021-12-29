package com.plcoding.soundrecognition.server

import com.plcoding.soundrecognition.data.models.*
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.GET




interface SoundService {

    @GET("api/sounds/soundsInfo")
    suspend fun getSounds(): Response<List<DataSound>>

    @GET("api/sounds/soundTypes")
    suspend fun getTypes(): Response<List<SoundType>>

    @DELETE("api/sounds/{id}")
    suspend fun deleteSound( @Path("id") id:String): Response<ServerMessage>

    @GET("api/sounds/{id}")
    suspend fun getSound( @Path("id") id:String): Response<DataSound>

    @Headers("Content-Type: application/json")
    @POST("api/sounds")
    suspend fun postSound(@Body sound: DataSound): Response<DataSound>

    @Headers("Content-Type: application/json")
    @POST("api/sounds/checkTime")
    suspend fun checkSoundTimeDomain(@Body sound: DataSound): Response<List<Pair<SoundType, SoundsTimeCoefficients>>>

    @Headers("Content-Type: application/json")
    @POST("api/sounds/checkPowerSpectrum")
    suspend fun checkSoundPowerSpectrum(@Body sound: DataSound): Response<List<Pair<SoundType, PowerSpectrumCoefficient>>>

    @Headers("Content-Type: application/json")
    @POST("api/sounds/checkFrequency")
    suspend fun checkSoundFrequencyDomain(@Body sound: DataSound): Response<List<Pair<SoundType, SoundsFreqCoefficients>>>

}