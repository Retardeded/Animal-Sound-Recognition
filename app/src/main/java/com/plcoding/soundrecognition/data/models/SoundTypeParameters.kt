package com.plcoding.soundrecognition.data.models

class SoundTypeParameters(val typeName:String, val parametersListInt:List<SoundTypeParameterInt>,
                          val parametersListDouble:List<SoundTypeParameterDouble>,
                          val zeroCrossingDensity:Integer) {
    enum class ParameterName {
        SignalEnvelope, RootMeanSquareEnergy, PowerSpectrum, SpectralCentroids, SpectralFluxes, SpectralRollOffPoints
    }

    override fun toString(): String {
        return  "$parametersListInt\n" +
                "$parametersListDouble\n" +
                "zeroCrossingDensity=$zeroCrossingDensity\n"
    }


}