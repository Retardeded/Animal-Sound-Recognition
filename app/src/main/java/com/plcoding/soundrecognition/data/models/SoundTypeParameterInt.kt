package com.plcoding.soundrecognition.data.models

class SoundTypeParameterInt(val parameterValues:List<Integer>, val name:SoundTypeParameters.ParameterName) {
    override fun toString(): String {
        return "$name=$parameterValues\n"
    }
}