package com.plcoding.soundrecognition.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.soundrecognition.data.models.DataSound
import com.plcoding.soundrecognition.data.models.SoundType
import com.plcoding.soundrecognition.data.models.SoundsTimeCoefficients
import com.plcoding.soundrecognition.util.DispatcherProvider
import com.plcoding.soundrecognition.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val repository: MainRepository,
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
            when(val ratesResponse = repository.getSounds()) {
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
            when(val ratesResponse = repository.getTypes()) {
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

    fun getSound(id: String) {
        viewModelScope.launch(dispatchers.io) {
            _conversion.value = SoundEvent.Loading
            when(val ratesResponse = repository.getSound(id)) {

            }
        }
    }

    fun postSound(sound:DataSound) {
        viewModelScope.launch(dispatchers.io) {
            _conversion.value = SoundEvent.Loading
            when(val ratesResponse = repository.postSound(sound)) {
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

    fun deleteSound(id: String) {
        viewModelScope.launch(dispatchers.io) {
            _conversion.value = SoundEvent.Loading
            when(val ratesResponse = repository.deleteSound(id)) {
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
            when(val ratesResponse = repository.checkSoundTimeDomain(sound)) {
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
            when(val ratesResponse = repository.checkSoundPowerSpectrum(sound)) {
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
            when(val ratesResponse = repository.checkSoundFrequencyDomain(sound)) {
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