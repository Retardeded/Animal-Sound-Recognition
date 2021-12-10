package com.plcoding.currencyconverter.server

import android.widget.TextView
import com.jjoe64.graphview.series.DataPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SoundServiceHandler {
    lateinit var service: CurrencyApi
    //var okHttpClient: OkHttpClient? = null
    // tutaj ustaw swoje lokalne ip
    val ipString = "http://192.168.1.3:8080"
    //.baseUrl("http://10.0.0.5:8080/")
    //.baseUrl("http://192.168.1.3:8080/")
    init {
        createClient()
    }
    fun createClient() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ipString)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(CurrencyApi::class.java)
    }

    suspend fun getSound(textTest: TextView, animalNameText: TextView) {
        val id = animalNameText.text.toString()
        val response = service.getSound(id)
        GlobalScope.launch(Dispatchers.Main) {
            if (response.isSuccessful) {
                textTest.text = response.toString()
                val soundData = response.body()!!
                val stringBuilder = soundData.toString();
                textTest.text = stringBuilder
                //dataGraphs.currentRecordTimeDomain = loadDataSound(soundData.pointsInGraphs, soundData.timeDomainPoints, false)
            }
            else {
                val text = "MSG:" + response.message() + "CAUSE: " + response.errorBody()
                textTest.text = text
            }

        }

    }

    /*
    private fun loadDataSound(pointsInGraphs:Long,soundData:List<DataPoint>, isFreqDomain:Boolean): MutableList<DataGraph> {
        val dataGraphs: MutableList<DataGraph> = mutableListOf()
        var pointsInGraphs = pointsInGraphs
        var numberOfGraphs = (soundData.size / pointsInGraphs)
        if(isFreqDomain) {
            numberOfGraphs = 1
            pointsInGraphs = soundData.size.toLong()-1
        }

        println(numberOfGraphs)
        println(pointsInGraphs)
        println(soundData.size)

        for (i in 0..numberOfGraphs-1) {
            val graph = DataGraph(
                soundData.subList(
                    ((i * pointsInGraphs).toInt()),
                    ((i + 1) * pointsInGraphs).toInt()
                )
            )
            dataGraphs.add(graph)
        }
        return dataGraphs
    }

     */

    suspend fun deleteSound(textTest: TextView, animalNameText: TextView) {
        val id = animalNameText.text.toString()
        val response = service.deleteSound(id)
        GlobalScope.launch(Dispatchers.Main) {
            if (response.isSuccessful) {
                textTest.text = response.toString()
            }
            else {
                val text = "MSG:" + response.message() + "CAUSE: " + response.errorBody()
                textTest.text = text
            }
        }
    }
}