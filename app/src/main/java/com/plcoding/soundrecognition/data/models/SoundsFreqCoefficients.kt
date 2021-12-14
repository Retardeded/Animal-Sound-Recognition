package com.plcoding.soundrecognition.data.models

class SoundsFreqCoefficients(val centroidsCoefficient:Double, val fluxesCoefficient:Double,
                             val rollOffPointsCoefficient:Double, val mergedCoefficient:Double) {
    override fun toString(): String {
        return " Coefficients:\n" +
                " mergedCoefficient=$mergedCoefficient\n" +
                "  centroidsCoefficient=$centroidsCoefficient\n" +
                "  fluxesCoefficient=$fluxesCoefficient\n" +
                "  rollOffPointsCoefficient=$rollOffPointsCoefficient\n"
    }
}