package com.plcoding.currencyconverter.data.models

import com.jjoe64.graphview.series.DataPoint


class DataGraph(dataPoints:List<DataPoint>) {
    val dataPoints: List<DataPoint> = dataPoints

    override fun toString(): String {
        return "Graph(dataPoints=${dataPoints.take(2)})"
    }
}