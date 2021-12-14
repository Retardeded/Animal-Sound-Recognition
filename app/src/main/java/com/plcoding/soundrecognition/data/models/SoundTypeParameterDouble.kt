package com.plcoding.soundrecognition.data.models

class SoundTypeParameterDouble(val parameterValues:List<Double>, val name:SoundTypeParameters.ParameterName) {
    override fun toString(): String {
        return "$name=${formatList(parameterValues)}\n"
    }

    fun formatList(list:List<Double>):List<Double> {

        val newList = mutableListOf<Double>()
        list.forEach {
            val number2digits:Double = String.format("%.3f", it).toDouble()
            newList.add(number2digits)
        }
        return newList
    }
}