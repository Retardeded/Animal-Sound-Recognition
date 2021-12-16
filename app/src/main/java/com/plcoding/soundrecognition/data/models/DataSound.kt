package com.plcoding.soundrecognition.data.models

import com.jjoe64.graphview.series.DataPoint


class DataSound(val title: String, private val type:String, private val durationMillis: Long,
                val pointsInGraphs: Long, val numOfGraphs: Long, val timeDomainPoints:List<DataPoint>) {
    private val id: Int? = null
    val dataSoundParameters: DataSoundParameters? = null

    override fun toString(): String {
        return "Sound:\n" +
                "id:$id\n" +
                "title:$title, " + "type:$type\n" +
                "durationInMillis:$durationMillis\n" +
                "pointsInGraphs:$pointsInGraphs, numsOfGraphs:$numOfGraphs\n" +
                "$dataSoundParameters\n"+
                "timePoints:${timeDomainPoints.takeLast(1)})\n"
    }
}