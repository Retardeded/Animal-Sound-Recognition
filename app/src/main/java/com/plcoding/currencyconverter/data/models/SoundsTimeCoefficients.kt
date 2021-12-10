package com.plcoding.currencyconverter.data.models

class SoundsTimeCoefficients(val envelopeCoefficient:Double, val energyCoefficient:Double,
                             val zeroCrossingCoefficient:Double, val mergedCoefficient:Double) {
    override fun toString(): String {
        return "Coefficients:(\nmergedCoefficient=$mergedCoefficient\n" +
                "envelopeCoefficient=$envelopeCoefficient\n" +
                "energyCoefficient=$energyCoefficient\n" +
                "zeroCrossingCoefficient=$zeroCrossingCoefficient\n"
    }
}