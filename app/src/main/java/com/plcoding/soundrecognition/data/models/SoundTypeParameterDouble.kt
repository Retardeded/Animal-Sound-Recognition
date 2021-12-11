package com.plcoding.soundrecognition.data.models

class SoundTypeParameterDouble(val parameterValues:List<Double>, val name:SoundTypeParameters.ParameterName) {
    override fun toString(): String {
        return "SoundTypeParameterInt($name=$parameterValues)"
    }
}