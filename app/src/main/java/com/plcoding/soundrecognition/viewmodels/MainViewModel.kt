package com.plcoding.soundrecognition.viewmodels

import android.widget.TextView
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjoe64.graphview.series.DataPoint
import com.plcoding.soundrecognition.data.models.DataGraph
import com.plcoding.soundrecognition.data.models.DataGraphs
import com.plcoding.soundrecognition.data.models.DataSound
import com.plcoding.soundrecognition.data.models.SoundType
import com.plcoding.soundrecognition.server.SoundServiceHandler
import com.plcoding.soundrecognition.util.DispatcherProvider
import com.plcoding.soundrecognition.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val soundServiceHandler: SoundServiceHandler,
    val dispatchers: DispatcherProvider
): ViewModel() {

    sealed class SoundEvent {
        class Success(val resultText: String): SoundEvent()
        class Failure(val errorText: String): SoundEvent()
        object Loading : SoundEvent()
        object Empty : SoundEvent()
    }


    private val _conversion = MutableStateFlow<SoundEvent>(SoundEvent.Empty)
    val conversion: StateFlow<SoundEvent> = _conversion

    fun getSounds() {
        viewModelScope.launch(dispatchers.io) {
            _conversion.value = SoundEvent.Loading
            when(val ratesResponse = soundServiceHandler.getSounds()) {
                is Resource.Error -> _conversion.value = SoundEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val list = ratesResponse.data!!
                    _conversion.value = SoundEvent.Success(
                        "$list"
                    )
                }
            }
        }
    }

    fun getTypes() {
        viewModelScope.launch(dispatchers.io) {
            _conversion.value = SoundEvent.Loading
            when(val ratesResponse = soundServiceHandler.getTypes()) {
                is Resource.Error -> _conversion.value = SoundEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val list = ratesResponse.data!!
                    _conversion.value = SoundEvent.Success(
                        "$list"
                    )
                }
            }
        }
    }

    fun getSound(id: String, dataGraphs: DataGraphs) {
        viewModelScope.launch(dispatchers.io) {
            _conversion.value = SoundEvent.Loading
            when(val ratesResponse = soundServiceHandler.getSound(id)) {
                is Resource.Error -> {
                    if(ratesResponse.message == "") {
                        _conversion.value = SoundEvent.Failure(
                            "Sound with that name was not found"
                        )
                    } else {
                        _conversion.value = SoundEvent.Failure(ratesResponse.message!!)
                    }
                }
                is Resource.Success -> {
                    val soundData = ratesResponse.data!!
                    dataGraphs.currentRecordTimeDomain = loadDataSound(soundData.pointsInGraphs, soundData.timeDomainPoints, false)
                    dataGraphs.numOfGraphs = soundData.numOfGraphs
                    dataGraphs.pointsInGraphs = soundData.pointsInGraphs
                    _conversion.value = SoundEvent.Success(
                        "$soundData"
                    )
                }
            }
        }
    }

    private fun loadDataSound(pointsInGraphs:Long, soundData:List<DataPoint>, isFreqDomain:Boolean): MutableList<DataGraph> {
        val dataGraphs: MutableList<DataGraph> = mutableListOf()
        var pointsInGraphs = pointsInGraphs
        var numberOfGraphs = (soundData.size / pointsInGraphs)
        if(isFreqDomain) {
            numberOfGraphs = 1
            pointsInGraphs = soundData.size.toLong()-1
        }

        println(numberOfGraphs)
        println(pointsInGraphs)
        println(soundData.size)

        for (i in 0..numberOfGraphs-1) {
            val graph = DataGraph(
                soundData.subList(
                    ((i * pointsInGraphs).toInt()),
                    ((i + 1) * pointsInGraphs).toInt()
                )
            )
            dataGraphs.add(graph)
        }
        return dataGraphs
    }


    fun postSound(sound:DataSound) {
        viewModelScope.launch(dispatchers.io) {
            _conversion.value = SoundEvent.Loading
            when(val ratesResponse = soundServiceHandler.postSound(sound)) {
                is Resource.Error -> {
                    val status = ratesResponse.message
                    println("Messsage $status")
                    println("BODY ${ratesResponse.data}")
                    if(ratesResponse.message == "") {
                        _conversion.value = SoundEvent.Failure(
                            "Sound with that name already exists"
                        )
                    } else {
                        _conversion.value = SoundEvent.Failure(ratesResponse.message!!)
                    }
                }
                is Resource.Success -> {
                    val list = ratesResponse.data!!
                    _conversion.value = SoundEvent.Success(
                        "$list"
                    )
                }
            }
        }
    }

    fun deleteSound(id:String) {
        viewModelScope.launch(dispatchers.io) {
            _conversion.value = SoundEvent.Loading
            when(val ratesResponse = soundServiceHandler.deleteSound(id)) {
                is Resource.Error -> {
                    if(ratesResponse.message == "") {
                        _conversion.value = SoundEvent.Failure(
                            "Sound with that name was not found"
                        )
                    } else {
                        _conversion.value = SoundEvent.Failure(ratesResponse.message!!)
                    }
                }
                is Resource.Success -> {
                    val list = ratesResponse.data!!
                    _conversion.value = SoundEvent.Success(
                        "$list"
                    )
                }
            }
        }
    }

    fun formatList(list:List<Pair<SoundType, Any>>):String {
        var text = "Most Similar Sound Types:\n"
        var index = 1

        list.forEach {
            val type = it.first
            val coefficient = it.second

            text += "$index - ${type.name}\n$coefficient"
            index++
        }

        return text
    }

    fun checkSoundTimeDomain(sound: DataSound) {
        viewModelScope.launch(dispatchers.io) {
            _conversion.value = SoundEvent.Loading
            when(val ratesResponse = soundServiceHandler.checkSoundTimeDomain(sound)) {
                is Resource.Error -> _conversion.value = SoundEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val list = ratesResponse.data!!
                    _conversion.value = SoundEvent.Success(
                        formatList(list)
                    )
                }
            }
        }
    }

    fun checkSoundPowerSpectrum(sound: DataSound) {
        viewModelScope.launch(dispatchers.io) {
            _conversion.value = SoundEvent.Loading
            when(val ratesResponse = soundServiceHandler.checkSoundPowerSpectrum(sound)) {
                is Resource.Error -> _conversion.value = SoundEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val list = ratesResponse.data!!
                    _conversion.value = SoundEvent.Success(
                        formatList(list)
                    )
                }
            }
        }
    }

    fun checkSoundFrequencyDomain(sound: DataSound) {
        viewModelScope.launch(dispatchers.io) {
            _conversion.value = SoundEvent.Loading
            when(val ratesResponse = soundServiceHandler.checkSoundFrequencyDomain(sound)) {
                is Resource.Error -> _conversion.value = SoundEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val list = ratesResponse.data!!
                    _conversion.value = SoundEvent.Success(
                        formatList(list)
                    )
                }
            }
        }
    }
}