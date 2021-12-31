package com.plcoding.soundrecognition.data.models

data class DataSoundParameters(val typeName:String, val signalEnvelope:List<Integer>,
                          val rootMeanSquareEnergy:List<Integer>, val zeroCrossingDensity:List<Integer>,
                          val powerSpectrum:List<Integer>, val spectralCentroids:List<Double>,
                          val spectralFluxes:List<Integer>, val spectralRollOffPoints:List<Double>) {
    override fun toString(): String {
        return "Parameters:\n" +
                "signalEnvelope=$signalEnvelope \n " +
                "rootMeanSquareEnergy=$rootMeanSquareEnergy \n" +
                "zeroCrossingDensity=$zeroCrossingDensity \n" +
                "powerSpectrum=$powerSpectrum \n " +
                "spectralCentroids=${formatList(spectralCentroids)} \n " +
                "spectralFluxes=$spectralFluxes \n " +
                "spectralRollOffPoints=$spectralRollOffPoints"
    }

    fun formatList(list:List<Double>):List<Double> {

        val newList = mutableListOf<Double>()
        list.forEach {
            val number2digits:Double = String.format("%.3f", it).toDouble()
            newList.add(number2digits)
        }
        return newList
    }

    fun Double.format(digits: Int) = "%.${digits}f".format(this)


}