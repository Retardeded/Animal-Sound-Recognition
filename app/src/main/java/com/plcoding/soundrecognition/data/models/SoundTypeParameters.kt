package com.plcoding.soundrecognition.data.models

class SoundTypeParameters(val typeName:String, val parametersListInt:List<SoundTypeParameterInt>,
                          val parametersListDouble:List<SoundTypeParameterDouble>) {
    enum class ParameterName {
        SignalEnvelope, RootMeanSquareEnergy, ZeroCrossingDensity, PowerSpectrum, SpectralCentroids, SpectralFluxes, SpectralRollOffPoints
    }

    override fun toString(): String {
        return  "$parametersListInt\n" +
                "$parametersListDouble\n"
    }


}