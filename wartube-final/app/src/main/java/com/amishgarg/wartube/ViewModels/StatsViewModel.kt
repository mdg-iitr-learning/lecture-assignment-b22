package com.amishgarg.wartube.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

import com.amishgarg.wartube.livedata.YoutubeRepository


class StatsViewModel: ViewModel() {



    val subsData : LiveData<List<Int>> by lazy {
        YoutubeRepository().getSubs()
    }


}























/*    private var subsP : Int = 0
    private var subsT : Int = 0
    private var diff : Int = subsP-subsT

    val apiService = ApiClient.getClient().create(ApiInterface::class.java)

    fun getSubsT() : Int {
        val qMap = HashMap<String, String>()
        qMap["part"] = "snippet,contentDetails,statistics"
        qMap["id"] = CHANNEL_ID_TS
        qMap["key"] = GOOGLE_YOUTUBE_API_KEY

        var call = apiService.getSubs(qMap)
        call.enqueue(object : Callback<ChannelResponse> {
            override fun onResponse(call: Call<ChannelResponse>, response: Response<ChannelResponse>) {

                Log.d("URL:", response.raw().request().url().toString())
                val channels = response.body()!!.channels
                subsT = channels[0].statistics.subscriberCount
                Log.d("YoutubeViewModel", subsT.toString() + "")
            }
            override fun onFailure(call: Call<ChannelResponse>, t: Throwable) {
                Log.d("YoutubeViewModel", t.toString())
            }
        })

        return subsT
    }

    fun getSubsP() : Int{
        val qMap2 = HashMap<String, String>()
        qMap2["part"] = "snippet,contentDetails,statistics"
        qMap2["id"] = CHANNEL_ID_PDP
        qMap2["key"] = GOOGLE_YOUTUBE_API_KEY

        val call2 = apiService.getSubs(qMap2)
        call2.enqueue(object : Callback<ChannelResponse> {
            override fun onResponse(call: Call<ChannelResponse>, response: Response<ChannelResponse>) {

                val channels = response.body()!!.channels
                subsP = channels[0].statistics.subscriberCount
                Log.d("GEEK", subsP.toString() + "")
             // graphView.addSeries(series)
            }

            override fun onFailure(call: Call<ChannelResponse>, t: Throwable) {
                Log.d("GEEK", t.toString())
            }
        })
        return subsP
    }*/
