package com.plcoding.soundrecognition.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.plcoding.soundrecognition.server.SoundService
import com.plcoding.soundrecognition.server.SoundServiceHandler
import com.plcoding.soundrecognition.util.DispatcherProvider
import com.plcoding.soundrecognition.soundprocessing.GraphHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "http://192.168.1.3:8080"

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCurrencyApi(): SoundService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
            .setLenient()
            .create()))
        .build()
        .create(SoundService::class.java)

    @Singleton
    @Provides
    fun provideSoundServiceHandler(api: SoundService): SoundServiceHandler = SoundServiceHandler(api)

    @Singleton
    @Provides
    fun provideGraphViewModel(): GraphHandler = GraphHandler()

    // tutaj dodac provide RecordHandler

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}