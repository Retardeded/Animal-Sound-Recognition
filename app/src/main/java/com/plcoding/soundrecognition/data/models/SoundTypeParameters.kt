package com.plcoding.soundrecognition.data.models

class SoundTypeParameters(val typeName:String, val parametersListInt:List<SoundTypeParameterInt>,
                          val parametersListDouble:List<SoundTypeParameterDouble>,
                          val zeroCrossingDensity:Integer) {
    enum class ParameterName {
        SignalEnvelope, RootMeanSquareEnergy, PowerSpectrum, SpectralCentroids, SpectralFluxes, SpectralRollOffPoints
    }

    override fun toString(): String {
        return "SoundTypeParameters(typeName='$typeName'," +
                " parametersListInt=$parametersListInt," +
                " parametersListDouble=$parametersListDouble," +
                " zeroCrossingDensity=$zeroCrossingDensity)"
    }


}