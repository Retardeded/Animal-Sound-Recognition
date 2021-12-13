package com.plcoding.soundrecognition.soundprocessing


import android.media.AudioRecord
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.plcoding.soundrecognition.data.models.DataGraph
import com.plcoding.soundrecognition.data.models.DataGraphs
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BaseSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.plcoding.soundrecognition.fftpack.RealDoubleFFT
import com.plcoding.soundrecognition.soundprocessing.RecordHandler.Companion.isPlaying
import com.plcoding.soundrecognition.soundprocessing.RecordHandler.Companion.isRecording
import com.plcoding.soundrecognition.soundprocessing.RecordHandler.Companion.mMinBufferSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class GraphHandler @ViewModelInject constructor(): ViewModel() {

    var dataGraphs: DataGraphs = DataGraphs()
    var mFullFreqSeries: BaseSeries<DataPoint>? = null
    var mAmplitudeSeries: BaseSeries<DataPoint>? = null
    var mTimeSeries: BaseSeries<DataPoint>? = null
    var transformer: RealDoubleFFT? = null

    fun initGraphView(graphTime: GraphView, graphAmplitude: GraphView, graphFreqFull: GraphView) {
        graphTime.title = "Time Domain"
        graphTime.viewport.setMaxY(10000.0)
        graphTime.viewport.setMinY(-10000.0)
        graphTime.viewport.isYAxisBoundsManual = true
        mTimeSeries = LineGraphSeries<DataPoint>(arrayOf<DataPoint>())

        mAmplitudeSeries = LineGraphSeries<DataPoint>(arrayOf<DataPoint>())
        graphAmplitude.title = "Frequency Domain Amplitude"
        graphAmplitude.viewport.setMaxY(375000.0)
        graphAmplitude.viewport.setMinY(-375000.0)
        graphAmplitude.viewport.isYAxisBoundsManual = true

        graphFreqFull.title = "Full Signal Frequency Domain"
        mFullFreqSeries = LineGraphSeries<DataPoint>(arrayOf<DataPoint>())
        graphFreqFull.viewport.setMaxY(250000.0)
        graphFreqFull.viewport.setMinY(-250.0)
        graphFreqFull.viewport.isYAxisBoundsManual = true

        if (graphTime.series.count() > 0) {
            graphTime.removeAllSeries()
        }
        graphTime.addSeries(mTimeSeries)

        if (graphAmplitude.series.count() > 0) {
            graphAmplitude.removeAllSeries()
        }
        graphAmplitude.addSeries(mAmplitudeSeries)

        if (graphFreqFull.series.count() > 0) {
            graphFreqFull.removeAllSeries()
        }
        graphFreqFull.addSeries(mFullFreqSeries)
    }

    fun updateGraphView(mAudioRecord: AudioRecord) {
        val audioData = ShortArray(mMinBufferSize)
        var index = 0
        val num = audioData.size
        val dataAmplitudeFullSignal = DoubleArray(num/2)
        while (isRecording) {
            val read = mAudioRecord!!.read(audioData, 0, mMinBufferSize)
            if (read != AudioRecord.ERROR_INVALID_OPERATION && read != AudioRecord.ERROR_BAD_VALUE) {
                //os?.write(audioData, 0, mMinBufferSize);
                val dataAmplitude = arrayOfNulls<DataPoint>(num/2)
                val dataTime = arrayOfNulls<DataPoint>(num)
                // apply Fast Fourier Transform here
                transformer = RealDoubleFFT(num)
                val toTransform = DoubleArray(num)
                for (i in 0 until num) {
                    toTransform[i] = audioData[i].toDouble() / num
                    //toTransform[i] = audioData[i].toDouble()
                }
                transformer!!.ft(toTransform)
                // the real part of k-th complex FFT coeffients is x[2*k-1];
                // <br>
                //the imaginary part of k-th complex FFT coeffients is x[2*k-2].
                for (i in 0 until num/2) {
                    //output_power[i] = (real_output[i] * real_output[i] + imaginary_output[i] * imaginary_output[i]) / real_output.length;
                    dataAmplitude[i] = DataPoint(i.toDouble(), toTransform[i*2+1] * toTransform[i*2+1] + toTransform[i*2] * toTransform[i*2])
                    dataAmplitudeFullSignal[i] += dataAmplitude[i]!!.y
                }

                for (i in 0 until num) {
                    dataTime[i] = DataPoint(i.toDouble(), audioData[i].toDouble())
                }


                val listTime: List<DataPoint> = dataTime.toList().filterNotNull()
                index++
                dataGraphs.currentRecordTimeDomain.add(DataGraph(listTime))

                //println(listTime)

                GlobalScope.launch( Dispatchers.Main ){
                    mAmplitudeSeries!!.resetData(dataAmplitude)
                    mTimeSeries!!.resetData(dataTime)
                }
            }

        }

        visualizePowerSpectrum(dataAmplitudeFullSignal, index)
        dataGraphs.pointsInGraphs = audioData.size.toLong()
        dataGraphs.numOfGraphs = index.toLong()
    }

    fun replayGraphView(mAudioRecord:AudioRecord): Boolean {
        var index = 0
        val audioData = ShortArray(mMinBufferSize)
        val numOfGraphs = dataGraphs.numOfGraphs.toInt()
        val num = dataGraphs.pointsInGraphs.toInt()
        val dataAmplitudeFullSignal = DoubleArray((num/2).toInt())

        while (isPlaying && index < numOfGraphs) {
            val read = mAudioRecord!!.read(audioData, 0, mMinBufferSize)
            val dataTime = arrayOfNulls<DataPoint>(num)
            val dataAmplitude = arrayOfNulls<DataPoint>(num/2)
            for (i in 0 until num) {
                dataTime[i] = dataGraphs.currentRecordTimeDomain[index].dataPoints[i]
            }
            transformer = RealDoubleFFT(num)
            val toTransform = DoubleArray(num)
            for (i in 0 until num) {
                toTransform[i] = dataTime[i]!!.y / num
            }
            transformer!!.ft(toTransform)
            for (i in 0 until num/2) {
                //output_power[i] = (real_output[i] * real_output[i] + imaginary_output[i] * imaginary_output[i]) / real_output.length;
                dataAmplitude[i] = DataPoint(i.toDouble(), toTransform[i*2+1] * toTransform[i*2+1] + toTransform[i*2] * toTransform[i*2])
                dataAmplitudeFullSignal[i] += dataAmplitude[i]!!.y
            }

            GlobalScope.launch( Dispatchers.Main ){
                mTimeSeries!!.resetData(dataTime)
                mAmplitudeSeries!!.resetData(dataAmplitude)
            }
            index++
        }

        visualizePowerSpectrum(dataAmplitudeFullSignal, index)
        return true
    }

    private fun visualizePowerSpectrum(dataAmplitudeFullSignal: DoubleArray, index: Int) {
        val n = dataAmplitudeFullSignal.size
        val data = arrayOfNulls<DataPoint>(n)
        for (i in data.indices) {
            data[i] = DataPoint(i.toDouble(), dataAmplitudeFullSignal[i] / index)
        }
        GlobalScope.launch(Dispatchers.Main) {
            mFullFreqSeries!!.resetData(data)
        }
    }


}