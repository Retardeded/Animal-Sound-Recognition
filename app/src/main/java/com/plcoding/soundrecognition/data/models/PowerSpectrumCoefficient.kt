package com.plcoding.soundrecognition.data.models

class PowerSpectrumCoefficient(val powerSpectrumCoefficient:Double, val mergedCoefficient:Double) {
    override fun toString(): String {
        return " Coefficients:\n" +
                " mergedCoefficient=$mergedCoefficient\n" +
                "  powerSpectrumCoefficient=$powerSpectrumCoefficient\n"
    }
}