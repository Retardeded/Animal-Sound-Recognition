package com.plcoding.soundrecognition.soundprocessing

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import android.widget.TextView
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel

import com.jjoe64.graphview.series.DataPoint
import com.plcoding.soundrecognition.data.models.DataSound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

const val SAMPLE_RATE = 10100
const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

class RecordHandler @ViewModelInject constructor(
    val graphHandler: GraphHandler
): ViewModel() {

    private var mAudioRecord: AudioRecord? = null
    private var recorder: MediaRecorder? = null
    private var mMediaPlayer: MediaPlayer? = null
    private var mRecordThread: Thread? = null
    var soundStartingTime:Long = 0
    var currentDuration:Long = 0


    fun startPlaying(textTest: TextView, animalNameText: TextView, fileName:String) {
        graphHandler.mAmplitudeSeries?.resetData(arrayOf<DataPoint>())
        graphHandler.mTimeSeries?.resetData(arrayOf<DataPoint>())
        graphHandler.mFullFreqSeries?.resetData(arrayOf<DataPoint>())

        val sound = createDataSound(true, animalNameText)
        val stringBuilder = sound.toString()
        GlobalScope.launch( Dispatchers.Main ){
            textTest.text = stringBuilder
        }

        mMediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e("", "prepare() failed")
            }
        }
        isPlaying = true
        mAudioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, mMinBufferSize)
        mAudioRecord!!.startRecording()

        val playedOut = graphHandler.replayGraphView(mAudioRecord!!)
        if(playedOut) {
            stopPlaying()
        }
    }

    fun stopPlaying() {
        mMediaPlayer?.release()
        mMediaPlayer = null
        isPlaying = false
        Thread.sleep(100)

        mAudioRecord?.stop()
        mRecordThread = null
        mAudioRecord?.release()
        mAudioRecord = null
    }

    fun startRecording(fileName:String) {
        graphHandler.dataGraphs.currentRecordTimeDomain.clear()
        mAudioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT,
            mMinBufferSize
        )
        mAudioRecord!!.startRecording()
        isRecording = true

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e("tag", "prepare() failed")
            }

            start()
        }

        mRecordThread = Thread(Runnable { graphHandler.updateGraphView(mAudioRecord!!) })
        mRecordThread!!.start()
        soundStartingTime = System.currentTimeMillis()
    }

    fun stopRecording() {
        currentDuration = System.currentTimeMillis() - soundStartingTime
        if (mAudioRecord != null) {
            if (isRecording) {
                mAudioRecord?.stop()
                isRecording = false
                Thread.sleep(100)
                mRecordThread = null
            }
            mAudioRecord?.release()
            mAudioRecord = null
            graphHandler.mAmplitudeSeries?.resetData(arrayOf<DataPoint>())
            graphHandler.mTimeSeries?.resetData(arrayOf<DataPoint>())
        }

        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    fun createDataSound(includeGraph:Boolean, animalNameText: TextView): DataSound {
        val timePoints: MutableList<DataPoint> = mutableListOf()
        if(includeGraph) {
            for (graphs in graphHandler.dataGraphs.currentRecordTimeDomain) {
                timePoints.addAll(graphs.dataPoints)
            }
        }

        val sound = DataSound(animalNameText.text.toString(), animalNameText.text.toString(), currentDuration, graphHandler.dataGraphs.pointsInGraphs, graphHandler.dataGraphs.numOfGraphs, timePoints)
        return sound
    }

    companion object {
        val mMinBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)
        var isPlaying = false
        var isRecording = false
    }
}