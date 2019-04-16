package com.amishgarg.wartube.livedata

import android.os.AsyncTask
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amishgarg.wartube.Model.YoutubeModels.ChannelResponse
import com.amishgarg.wartube.rest.ApiClient
import com.amishgarg.wartube.rest.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class YoutubeRepository {

    //todo: make it async task, IMP...

    private val GOOGLE_YOUTUBE_API_KEY = "AIzaSyDK__bGjXkyQzUV--1jiLfpn3h4gRrUtK4"
    private val CHANNEL_ID_TS = "UCq-Fj5jknLsUf-MWSy4_brA"
    private val CHANNEL_ID_PDP = "UC-lHJZR3Gqxm24_Vd_AJ5Yw"

    lateinit var  apiInterface: ApiInterface

    var subsP : Int = 0;
    var subsT : Int = 0;


    private lateinit var mRunnable:Runnable
    inline fun runnable(crossinline body: Runnable.() -> Unit) = object : Runnable {
        override fun run() = this.body()
    }

    fun getInstance() : YoutubeRepository
    {
        return YoutubeRepository()
    }



    fun getSubs(): LiveData<List<Int>>
    {
        apiInterface =  ApiClient.getClient().create(ApiInterface::class.java)

        val subsData : MutableLiveData<List<Int>> = MutableLiveData()

        val qMap = HashMap<String, String>()
        qMap["part"] = "snippet,contentDetails,statistics"
        qMap["id"] = CHANNEL_ID_TS
        qMap["key"] = GOOGLE_YOUTUBE_API_KEY

        val qMap2 = HashMap<String, String>()
        qMap2["part"] = "snippet,contentDetails,statistics"
        qMap2["id"] = CHANNEL_ID_PDP
        qMap2["key"] = GOOGLE_YOUTUBE_API_KEY


        var shouldStopLoop = false
        val handler = Handler()
        val runnableCode = runnable {
            // do something
            apiInterface.getSubs(qMap).enqueue(object : Callback<ChannelResponse>{
                override fun onResponse(call: Call<ChannelResponse>, response: Response<ChannelResponse>) {
                    Log.d("URL:", response.raw().request().url().toString())
                    val channels = response.body()!!.channels
                    subsT = channels[0].statistics.subscriberCount
                    Log.d("YoutubeViewModel", "${subsT}")
                }

                override fun onFailure(call: Call<ChannelResponse>, t: Throwable) {
                    Log.d("YoutubeViewModel", t.toString())      }
            })

            apiInterface.getSubs(qMap2).enqueue(object : Callback<ChannelResponse>{
                override fun onResponse(call: Call<ChannelResponse>, response: Response<ChannelResponse>) {
                    Log.d("URL:", response.raw().request().url().toString())
                    val channels = response.body()!!.channels
                    subsP = channels[0].statistics.subscriberCount

                    Log.d("YoutubeViewModel", "${subsP}")
                }

                override fun onFailure(call: Call<ChannelResponse>, t: Throwable) {
                    Log.d("YoutubeViewModel", t.toString())      }
            })

            subsData.value = listOf(subsP, subsT, (subsP-subsT))
            Log.d("GEEK", "called getSubsData")
            if(!shouldStopLoop)
            {
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnableCode)
        return subsData
    }



}

