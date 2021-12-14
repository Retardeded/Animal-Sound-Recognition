package com.plcoding.soundrecognition.data.models

class SoundType(val name:String, val dataSounds:List<DataSound>, val soundTypeParameters: SoundTypeParameters) {
    override fun toString(): String {
        return "SoundType:\n" +
                "Type Name:$name\n\n" +
                "Consists of these sounds:\n\n" +
                "${dataSounds}\n\n" +
                "This Sound Type weighted average parameters:\n\n" +
                "$soundTypeParameters\n"
    }
}